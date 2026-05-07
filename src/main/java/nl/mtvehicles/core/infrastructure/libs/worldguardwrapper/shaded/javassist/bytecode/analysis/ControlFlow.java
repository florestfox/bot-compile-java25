package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.analysis;

import java.util.ArrayList;
import java.util.List;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtClass;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtMethod;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.BadBytecode;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.MethodInfo;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.stackmap.BasicBlock;

public class ControlFlow {
   private CtClass clazz;
   private MethodInfo methodInfo;
   private Block[] basicBlocks;
   private Frame[] frames;

   public ControlFlow(CtMethod method) throws BadBytecode {
      this(method.getDeclaringClass(), method.getMethodInfo2());
   }

   public ControlFlow(CtClass ctclazz, MethodInfo minfo) throws BadBytecode {
      this.clazz = ctclazz;
      this.methodInfo = minfo;
      this.frames = null;
      this.basicBlocks = (Block[])(new BasicBlock.Maker() {
         protected BasicBlock makeBlock(int pos) {
            return new Block(pos, ControlFlow.this.methodInfo);
         }

         protected BasicBlock[] makeArray(int size) {
            return new Block[size];
         }
      }).make(minfo);
      if (this.basicBlocks == null) {
         this.basicBlocks = new Block[0];
      }

      int size = this.basicBlocks.length;
      int[] counters = new int[size];

      for(int i = 0; i < size; ++i) {
         Block b = this.basicBlocks[i];
         b.index = i;
         b.entrances = new Block[b.incomings()];
         counters[i] = 0;
      }

      for(int i = 0; i < size; ++i) {
         Block b = this.basicBlocks[i];

         for(int k = 0; k < b.exits(); ++k) {
            Block e = b.exit(k);
            e.entrances[counters[e.index]++] = b;
         }

         Catcher[] catchers = b.catchers();

         for(int k = 0; k < catchers.length; ++k) {
            Block catchBlock = catchers[k].node;
            catchBlock.entrances[counters[catchBlock.index]++] = b;
         }
      }

   }

   public Block[] basicBlocks() {
      return this.basicBlocks;
   }

   public Frame frameAt(int pos) throws BadBytecode {
      if (this.frames == null) {
         this.frames = (new Analyzer()).analyze(this.clazz, this.methodInfo);
      }

      return this.frames[pos];
   }

   public Node[] dominatorTree() {
      int size = this.basicBlocks.length;
      if (size == 0) {
         return null;
      } else {
         Node[] nodes = new Node[size];
         boolean[] visited = new boolean[size];
         int[] distance = new int[size];

         for(int i = 0; i < size; ++i) {
            nodes[i] = new Node(this.basicBlocks[i]);
            visited[i] = false;
         }

         Access access = new Access(nodes) {
            BasicBlock[] exits(Node n) {
               return n.block.getExit();
            }

            BasicBlock[] entrances(Node n) {
               return n.block.entrances;
            }
         };
         nodes[0].makeDepth1stTree((Node)null, visited, 0, distance, access);

         do {
            for(int i = 0; i < size; ++i) {
               visited[i] = false;
            }
         } while(nodes[0].makeDominatorTree(visited, distance, access));

         ControlFlow.Node.setChildren(nodes);
         return nodes;
      }
   }

   public Node[] postDominatorTree() {
      int size = this.basicBlocks.length;
      if (size == 0) {
         return null;
      } else {
         Node[] nodes = new Node[size];
         boolean[] visited = new boolean[size];
         int[] distance = new int[size];

         for(int i = 0; i < size; ++i) {
            nodes[i] = new Node(this.basicBlocks[i]);
            visited[i] = false;
         }

         Access access = new Access(nodes) {
            BasicBlock[] exits(Node n) {
               return n.block.entrances;
            }

            BasicBlock[] entrances(Node n) {
               return n.block.getExit();
            }
         };
         int counter = 0;

         for(int i = 0; i < size; ++i) {
            if (nodes[i].block.exits() == 0) {
               counter = nodes[i].makeDepth1stTree((Node)null, visited, counter, distance, access);
            }
         }

         boolean changed;
         do {
            for(int i = 0; i < size; ++i) {
               visited[i] = false;
            }

            changed = false;

            for(int i = 0; i < size; ++i) {
               if (nodes[i].block.exits() == 0 && nodes[i].makeDominatorTree(visited, distance, access)) {
                  changed = true;
               }
            }
         } while(changed);

         ControlFlow.Node.setChildren(nodes);
         return nodes;
      }
   }

   public static class Block extends BasicBlock {
      public Object clientData = null;
      int index;
      MethodInfo method;
      Block[] entrances;

      Block(int pos, MethodInfo minfo) {
         super(pos);
         this.method = minfo;
      }

      protected void toString2(StringBuilder sbuf) {
         super.toString2(sbuf);
         sbuf.append(", incoming{");

         for(int i = 0; i < this.entrances.length; ++i) {
            sbuf.append(this.entrances[i].position).append(", ");
         }

         sbuf.append('}');
      }

      BasicBlock[] getExit() {
         return this.exit;
      }

      public int index() {
         return this.index;
      }

      public int position() {
         return this.position;
      }

      public int length() {
         return this.length;
      }

      public int incomings() {
         return this.incoming;
      }

      public Block incoming(int n) {
         return this.entrances[n];
      }

      public int exits() {
         return this.exit == null ? 0 : this.exit.length;
      }

      public Block exit(int n) {
         return (Block)this.exit[n];
      }

      public Catcher[] catchers() {
         List<Catcher> catchers = new ArrayList();

         for(BasicBlock.Catch c = this.toCatch; c != null; c = c.next) {
            catchers.add(new Catcher(c));
         }

         return (Catcher[])catchers.toArray(new Catcher[catchers.size()]);
      }
   }

   abstract static class Access {
      Node[] all;

      Access(Node[] nodes) {
         this.all = nodes;
      }

      Node node(BasicBlock b) {
         return this.all[((Block)b).index];
      }

      abstract BasicBlock[] exits(Node var1);

      abstract BasicBlock[] entrances(Node var1);
   }

   public static class Node {
      private Block block;
      private Node parent;
      private Node[] children;

      Node(Block b) {
         this.block = b;
         this.parent = null;
      }

      public String toString() {
         StringBuilder sbuf = new StringBuilder();
         sbuf.append("Node[pos=").append(this.block().position());
         sbuf.append(", parent=");
         sbuf.append(this.parent == null ? "*" : Integer.toString(this.parent.block().position()));
         sbuf.append(", children{");

         for(int i = 0; i < this.children.length; ++i) {
            sbuf.append(this.children[i].block().position()).append(", ");
         }

         sbuf.append("}]");
         return sbuf.toString();
      }

      public Block block() {
         return this.block;
      }

      public Node parent() {
         return this.parent;
      }

      public int children() {
         return this.children.length;
      }

      public Node child(int n) {
         return this.children[n];
      }

      int makeDepth1stTree(Node caller, boolean[] visited, int counter, int[] distance, Access access) {
         int index = this.block.index;
         if (visited[index]) {
            return counter;
         } else {
            visited[index] = true;
            this.parent = caller;
            BasicBlock[] exits = access.exits(this);
            if (exits != null) {
               for(int i = 0; i < exits.length; ++i) {
                  Node n = access.node(exits[i]);
                  counter = n.makeDepth1stTree(this, visited, counter, distance, access);
               }
            }

            distance[index] = counter++;
            return counter;
         }
      }

      boolean makeDominatorTree(boolean[] visited, int[] distance, Access access) {
         int index = this.block.index;
         if (visited[index]) {
            return false;
         } else {
            visited[index] = true;
            boolean changed = false;
            BasicBlock[] exits = access.exits(this);
            if (exits != null) {
               for(int i = 0; i < exits.length; ++i) {
                  Node n = access.node(exits[i]);
                  if (n.makeDominatorTree(visited, distance, access)) {
                     changed = true;
                  }
               }
            }

            BasicBlock[] entrances = access.entrances(this);
            if (entrances != null) {
               for(int i = 0; i < entrances.length; ++i) {
                  if (this.parent != null) {
                     Node n = getAncestor(this.parent, access.node(entrances[i]), distance);
                     if (n != this.parent) {
                        this.parent = n;
                        changed = true;
                     }
                  }
               }
            }

            return changed;
         }
      }

      private static Node getAncestor(Node n1, Node n2, int[] distance) {
         while(true) {
            if (n1 != n2) {
               if (distance[n1.block.index] < distance[n2.block.index]) {
                  n1 = n1.parent;
               } else {
                  n2 = n2.parent;
               }

               if (n1 != null && n2 != null) {
                  continue;
               }

               return null;
            }

            return n1;
         }
      }

      private static void setChildren(Node[] all) {
         int size = all.length;
         int[] nchildren = new int[size];

         for(int i = 0; i < size; ++i) {
            nchildren[i] = 0;
         }

         for(int i = 0; i < size; ++i) {
            Node p = all[i].parent;
            if (p != null) {
               ++nchildren[p.block.index];
            }
         }

         for(int i = 0; i < size; ++i) {
            all[i].children = new Node[nchildren[i]];
         }

         for(int i = 0; i < size; ++i) {
            nchildren[i] = 0;
         }

         for(int i = 0; i < size; ++i) {
            Node n = all[i];
            Node p = n.parent;
            if (p != null) {
               p.children[nchildren[p.block.index]++] = n;
            }
         }

      }
   }

   public static class Catcher {
      private Block node;
      private int typeIndex;

      Catcher(BasicBlock.Catch c) {
         this.node = (Block)c.body;
         this.typeIndex = c.typeIndex;
      }

      public Block block() {
         return this.node;
      }

      public String type() {
         return this.typeIndex == 0 ? "java.lang.Throwable" : this.node.method.getConstPool().getClassInfo(this.typeIndex);
      }
   }
}
