
package cz.simplejvm;

public class Instructions {
    public static final int aaload = 0x32;
    public static final int aastore = 0x53;
    public static final int aconst_null = 0x1;
    public static final int aload = 0x19;
    public static final int aload_0 = 0x2a;
    public static final int aload_1 = 0x2b;
    public static final int aload_2 = 0x2c;
    public static final int aload_3 = 0x2d;
    public static final int anewarray = 0xbd;
    public static final int areturn = 0xb0;
    public static final int arraylength = 0xbe;
    public static final int astore = 0x3a;
    public static final int astore_0 = 0x4b;
    public static final int astore_1 = 0x4c;
    public static final int astore_2 = 0x4d;
    public static final int astore_3 = 0x4e;
    public static final int athrow = 0xbf;
    public static final int baload = 0x33;
    public static final int bastore = 0x54;
    public static final int bipush = 0x10;
    public static final int breakpoint = 0xca;
    public static final int caload = 0x34;
    public static final int castore = 0x55;
    public static final int checkcast = 0xc0;
    public static final int d2f = 0x90;
    public static final int d2i = 0x8e;
    public static final int d2l = 0x8f;
    public static final int dadd = 0x63;
    public static final int daload = 0x31;
    public static final int dastore = 0x52;
    public static final int dcmpg = 0x98;
    public static final int dcmpl = 0x97;
    public static final int dconst_0 = 0x0e;
    public static final int dconst_1 = 0x0f;
    public static final int ddiv = 0x6f;
    public static final int dload = 0x18;
    public static final int dload_0 = 0x26;
    public static final int dload_1 = 0x27;
    public static final int dload_2 = 0x28;
    public static final int dload_3 = 0x29;
    public static final int dmul = 0x6b;
    public static final int dneg = 0x77;
    public static final int drem = 0x73;
    public static final int dreturn = 0xaf;
    public static final int dstore = 0x39;
    public static final int dstore_0 = 0x47;
    public static final int dstore_1 = 0x48;
    public static final int dstore_2 = 0x49;
    public static final int dstore_3 = 0x4a;
    public static final int dsub = 0x67;
    public static final int dup = 0x59;
    public static final int dup_x1 = 0x5a;
    public static final int dup_x2 = 0x5b;
    public static final int dup2 = 0x5c;
    public static final int dup2_x1 = 0x5d;
    public static final int dup2_x2 = 0x5e;
    public static final int f2d = 0x8d;
    public static final int f2i = 0x8b;
    public static final int f2l = 0x8c;
    public static final int fadd = 0x62;
    public static final int faload = 0x30;
    public static final int fastore = 0x51;
    public static final int fcmpg = 0x96;
    public static final int fcmpl = 0x95;
    public static final int fconst_0 = 0x0b;
    public static final int fconst_1 = 0x0c;
    public static final int fconst_2 = 0x0d;
    public static final int fdiv = 0x6e;
    public static final int fload = 0x17;
    public static final int fload_0 = 0x22;
    public static final int fload_1 = 0x23;
    public static final int fload_2 = 0x24;
    public static final int fload_3 = 0x25;
    public static final int fmul = 0x6a;
    public static final int fneg = 0x76;
    public static final int frem = 0x72;
    public static final int freturn = 0xae;
    public static final int fstore = 0x38;
    public static final int fstore_0 = 0x43;
    public static final int fstore_1 = 0x44;
    public static final int fstore_2 = 0x45;
    public static final int fstore_3 = 0x46;
    public static final int fsub = 0x66;
    public static final int getfield = 0xb4;
    public static final int getstatic = 0xb2;
    public static final int goto_ = 0xa7;
    public static final int goto_w = 0xc8;
    public static final int i2b = 0x91;
    public static final int i2c = 0x92;
    public static final int i2d = 0x87;
    public static final int i2f = 0x86;
    public static final int i2l = 0x85;
    public static final int i2s = 0x93;
    public static final int iadd = 0x60;
    public static final int iaload = 0x2e;
    public static final int iand = 0x7e;
    public static final int iastore = 0x4f;
    public static final int iconst_m1 = 0x2;
    public static final int iconst_0 = 0x3;
    public static final int iconst_1 = 0x4;
    public static final int iconst_2 = 0x5;
    public static final int iconst_3 = 0x6;
    public static final int iconst_4 = 0x7;
    public static final int iconst_5 = 0x8;
    public static final int idiv = 0x6c;
    public static final int if_acmpeq = 0xa5;
    public static final int if_acmpne = 0xa6;
    public static final int if_icmpeq = 0x9f;
    public static final int if_icmpge = 0xa2;
    public static final int if_icmpgt = 0xa3;
    public static final int if_icmple = 0xa4;
    public static final int if_icmplt = 0xa1;
    public static final int if_icmpne = 0xa0;
    public static final int ifeq = 0x99;
    public static final int ifge = 0x9c;
    public static final int ifgt = 0x9d;
    public static final int ifle = 0x9e;
    public static final int iflt = 0x9b;
    public static final int ifne = 0x9a;
    public static final int ifnonnull = 0xc7;
    public static final int ifnull = 0xc6;
    public static final int iinc = 0x84;
    public static final int iload = 0x15;
    public static final int iload_0 = 0x1a;
    public static final int iload_1 = 0x1b;
    public static final int iload_2 = 0x1c;
    public static final int iload_3 = 0x1d;
    public static final int impdep1 = 0xfe;
    public static final int impdep2 = 0xff;
    public static final int imul = 0x68;
    public static final int ineg = 0x74;
    public static final int instanceof_ = 0xc1;
    public static final int invokedynamic = 0xba;
    public static final int invokeinterface = 0xb9;
    public static final int invokespecial = 0xb7;
    public static final int invokestatic = 0xb8;
    public static final int invokevirtual = 0xb6;
    public static final int ior = 0x80;
    public static final int irem = 0x70;
    public static final int ireturn = 0xac;
    public static final int ishl = 0x78;
    public static final int ishr = 0x7a;
    public static final int istore = 0x36;
    public static final int istore_0 = 0x3b;
    public static final int istore_1 = 0x3c;
    public static final int istore_2 = 0x3d;
    public static final int istore_3 = 0x3e;
    public static final int isub = 0x64;
    public static final int iushr = 0x7c;
    public static final int ixor = 0x82;
    public static final int jsr = 0xa8;
    public static final int jsr_w = 0xc9;
    public static final int l2d = 0x8a;
    public static final int l2f = 0x89;
    public static final int l2i = 0x88;
    public static final int ladd = 0x61;
    public static final int laload = 0x2f;
    public static final int land = 0x7f;
    public static final int lastore = 0x50;
    public static final int lcmp = 0x94;
    public static final int lconst_0 = 0x9;
    public static final int lconst_1 = 0x0a;
    public static final int ldc = 0x12;
    public static final int ldc_w = 0x13;
    public static final int ldc2_w = 0x14;
    public static final int ldiv = 0x6d;
    public static final int lload = 0x16;
    public static final int lload_0 = 0x1e;
    public static final int lload_1 = 0x1f;
    public static final int lload_2 = 0x20;
    public static final int lload_3 = 0x21;
    public static final int lmul = 0x69;
    public static final int lneg = 0x75;
    public static final int lookupswitch = 0xab;
    public static final int lor = 0x81;
    public static final int lrem = 0x71;
    public static final int lreturn = 0xad;
    public static final int lshl = 0x79;
    public static final int lshr = 0x7b;
    public static final int lstore = 0x37;
    public static final int lstore_0 = 0x3f;
    public static final int lstore_1 = 0x40;
    public static final int lstore_2 = 0x41;
    public static final int lstore_3 = 0x42;
    public static final int lsub = 0x65;
    public static final int lushr = 0x7d;
    public static final int lxor = 0x83;
    public static final int monitorenter = 0xc2;
    public static final int monitorexit = 0xc3;
    public static final int multianewarray = 0xc5;
    public static final int new_ = 0xbb;
    public static final int newarray = 0xbc;
    public static final int nop = 0x0;
    public static final int pop = 0x57;
    public static final int pop2 = 0x58;
    public static final int putfield = 0xb5;
    public static final int putstatic = 0xb3;
    public static final int ret = 0xa9;
    public static final int return_ = 0xb1;
    public static final int saload = 0x35;
    public static final int sastore = 0x56;
    public static final int sipush = 0x11;
    public static final int swap = 0x5f;
    public static final int tableswitch = 0xaa;
    public static final int wide = 0xc4;

}
