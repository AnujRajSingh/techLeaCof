package com.ulap.util.tecleaApi.Algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class DaitchMokotoffSoundexEntry
{

  private static final String RTZ = "rtz";
  private static final String _45 = "45";
  private static final String _94 = "94";
  private static final String _43 = "43";
  private static final String _9 = "9";
  private static final String _2 = "2";
  private static final String _6 = "6";
  private static final String _66 = "66";
  private static final String _8 = "8";
  private static final String _54 = "54";
  private static final String _5 = "5";
  private static final String _7 = "7";
  private static final String _NC = "";
  private static final String _0 = "0";
  private static final String _1 = "1";
  private static final String _3 = "3";
  private static final String _4 = "4";
  private static final String ZHD = "zhd";
  private static final String ZD = "zd";
  private static final String ZDZH = "zdzh";
  private static final String ZDZ = "zdz";
  private static final String ZHDZH = "zhdzh";
  private static final String ZSH = "zsh";
  private static final String DSZ = "dsz";
  private static final String THS = "ths";
  private static final String Z = "z";
  private static final String ZSCH = "zsch";
  private static final String ZS = "zs";
  private static final String ZH = "zh";
  private static final String Y = "y";
  private static final String X = "x";
  private static final String W = "w";
  private static final String V = "v";
  private static final String UE = "ue";
  private static final String U = "u";
  private static final String UY = "uy";
  private static final String UJ = "uj";
  private static final String UI = "ui";
  private static final String T = "t";
  private static final String TSZ = "tsz";
  private static final String TZS = "tzs";
  private static final String TTZ = "ttz";
  private static final String TZ = "tz";
  private static final String TC = "tc";
  private static final String TTSZ = "ttsz";
  private static final String TTS = "tts";
  private static final String TS = "ts";
  private static final String TSH = "tsh";
  private static final String TSCH = "tsch";
  private static final String TRS = "trs";
  private static final String TRZ = "trz";
  private static final String TH = "th";
  private static final String TTSCH = "ttsch";
  private static final String TTCH = "ttch";
  private static final String TCH = "tch";
  private static final String S = "s";
  private static final String SZ = "sz";
  private static final String SD = "sd";
  private static final String SZD = "szd";
  private static final String SHD = "shd";
  private static final String SZT = "szt";
  private static final String SZCS = "szcs";
  private static final String SZCZ = "szcz";
  private static final String ST = "st";
  private static final String STSH = "stsh";
  private static final String STRS = "strs";
  private static final String STRZ = "strz";
  private static final String SC = "sc";
  private static final String STSCH = "stsch";
  private static final String STCH = "stch";
  private static final String SH = "sh";
  private static final String SCHD = "schd";
  private static final String SCHT = "scht";
  private static final String SHT = "sht";
  private static final String SHTSH = "shtsh";
  private static final String SHCH = "shch";
  private static final String SHTCH = "shtch";
  private static final String SCH = "sch";
  private static final String SCHTCH = "schtch";
  private static final String SCHTSH = "schtsh";
  private static final String SCHTSCH = "schtsch";
  private static final String R = "r";
  private static final String RS = "rs";
  private static final String RZ = "rz";
  private static final String Q = "q";
  private static final String PH = "ph";
  private static final String PF = "pf";
  private static final String P = "p";
  private static final String O = "o";
  private static final String OY = "oy";
  private static final String OJ = "oj";
  private static final String OI = "oi";
  private static final String N = "n";
  private static final String NM = "nm";
  private static final String M = "m";
  private static final String MN = "mn";
  private static final String L = "l";
  private static final String K = "k";
  private static final String KH = "kh";
  private static final String KS = "ks";
  private static final String J = "j";
  private static final String I = "i";
  private static final String IU = "iu";
  private static final String IO = "io";
  private static final String IE = "ie";
  private static final String IA = "ia";
  private static final String H = "h";
  private static final String G = "g";
  private static final String F = "f";
  private static final String FB = "fb";
  private static final String E = "e";
  private static final String EU = "eu";
  private static final String EJ = "ej";
  private static final String EY = "ey";
  private static final String EI = "ei";
  private static final String DT = "dt";
  private static final String D = "d";
  private static final String DZS = "dzs";
  private static final String DZH = "dzh";
  private static final String DZ = "dz";
  private static final String DSH = "dsh";
  private static final String DS = "ds";
  private static final String DRS = "drs";
  private static final String DRZ = "drz";
  private static final String CZS = "czs";
  private static final String CSZ = "csz";
  private static final String CS = "cs";
  private static final String CZ = "cz";
  private static final String C = "c";
  private static final String CK = "ck";
  private static final String CH = "ch";
  private static final String CHS = "chs";
  private static final String B = "b";
  private static final String A = "a";
  private static final String AU = "au";
  private static final String AY = "ay";
  private static final String AJ = "aj";
  private static final String AI = "ai";
  private static final String TSK = "tsk";
  
  
  private String name;
  private boolean vowel;
  private String first;
  private String before;
  private String other;
  private String[] alternate;


  public DaitchMokotoffSoundexEntry(String name, boolean vowel, String first, String before, String other, String[] alternate)
  {
    this.name = name;
    this.vowel = vowel;
    this.first = first;
    this.before = before;
    this.other = other;
    this.alternate = alternate;
    
  }
  
  private static Map<String, DaitchMokotoffSoundexEntry> entrySet = new HashMap<String, DaitchMokotoffSoundexEntry>(120);
  private static Map<Character, String[]> startsWithMap = new HashMap<Character, String[]>();
  static
  {
    entrySet.put(AI, new DaitchMokotoffSoundexEntry(AI, true, _0, _1, _NC, null));
    entrySet.put(AJ, new DaitchMokotoffSoundexEntry(AJ, true, _0, _1, _NC, null));
    entrySet.put(AY, new DaitchMokotoffSoundexEntry(AY, true, _0, _1, _NC, null));
    entrySet.put(AU, new DaitchMokotoffSoundexEntry(AU, true, _0, _7, _NC, null));
    entrySet.put(A, new DaitchMokotoffSoundexEntry(A, true, _0, _NC, _NC, null));
    entrySet.put(B, new DaitchMokotoffSoundexEntry(B, false, _7, _7, _7, null));
    entrySet.put(CHS, new DaitchMokotoffSoundexEntry(CHS, false, _5, _54, _54, null));
    entrySet.put(CH, new DaitchMokotoffSoundexEntry(CH, false, null, null, null, new String[] { KH, TCH }));
    entrySet.put(CK, new DaitchMokotoffSoundexEntry(CK, false, null, null, null, new String[] { K, TSK }));
    entrySet.put(C, new DaitchMokotoffSoundexEntry(C, false, null, null, null, new String[] { K, TZ}));
    entrySet.put(CZ, new DaitchMokotoffSoundexEntry(CZ, false, _4, _4, _4, null));
    entrySet.put(CS, new DaitchMokotoffSoundexEntry(CS, false, _4, _4, _4, null));
    entrySet.put(CSZ, new DaitchMokotoffSoundexEntry(CSZ, false, _4, _4, _4, null));
    entrySet.put(CZS, new DaitchMokotoffSoundexEntry(CZS, false, _4, _4, _4, null));
    entrySet.put(DRZ, new DaitchMokotoffSoundexEntry(DRZ, false, _4, _4, _4, null));
    entrySet.put(DRS, new DaitchMokotoffSoundexEntry(DRS, false, _4, _4, _4, null));
    entrySet.put(DS, new DaitchMokotoffSoundexEntry(DS, false, _4, _4, _4, null));
    entrySet.put(DSH, new DaitchMokotoffSoundexEntry(DSH, false, _4, _4, _4, null));
    entrySet.put(DSZ, new DaitchMokotoffSoundexEntry(DSZ, false, _4, _4, _4, null));
    entrySet.put(DZ, new DaitchMokotoffSoundexEntry(DZ, false, _4, _4, _4, null));
    entrySet.put(DZH, new DaitchMokotoffSoundexEntry(DZH, false, _4, _4, _4, null));
    entrySet.put(DZS, new DaitchMokotoffSoundexEntry(DZS, false, _4, _4, _4, null));
    entrySet.put(D, new DaitchMokotoffSoundexEntry(D, false, _3, _3, _3, null));
    entrySet.put(DT, new DaitchMokotoffSoundexEntry(DT, false, _3, _3, _3, null));
    entrySet.put(EI, new DaitchMokotoffSoundexEntry(EI, true, _0, _1, _NC, null));
    entrySet.put(EY, new DaitchMokotoffSoundexEntry(EY, true, _0, _1, _NC, null));
    entrySet.put(EJ, new DaitchMokotoffSoundexEntry(EJ, true, _0, _1, _NC, null));
    entrySet.put(EU, new DaitchMokotoffSoundexEntry(EU, true, _1, _1, _NC, null));
    entrySet.put(E, new DaitchMokotoffSoundexEntry(E, true, _0, _NC, _NC, null));
    entrySet.put(FB, new DaitchMokotoffSoundexEntry(FB, false, _7, _7, _7, null));
    entrySet.put(F, new DaitchMokotoffSoundexEntry(F, false, _7, _7, _7, null));
    entrySet.put(G, new DaitchMokotoffSoundexEntry(G, false, _5, _5, _5, null));
    entrySet.put(H, new DaitchMokotoffSoundexEntry(H, false, _5, _5, _NC, null));
    entrySet.put(IA, new DaitchMokotoffSoundexEntry(IA, true, _1, _NC, _NC, null));
    entrySet.put(IE, new DaitchMokotoffSoundexEntry(IE, true, _1, _NC, _NC, null));
    entrySet.put(IO, new DaitchMokotoffSoundexEntry(IO, true, _1, _NC, _NC, null));
    entrySet.put(IU, new DaitchMokotoffSoundexEntry(IU, true, _1, _NC, _NC, null));
    entrySet.put(I, new DaitchMokotoffSoundexEntry(I, true, _0, _NC, _NC, null));
    entrySet.put(J, new DaitchMokotoffSoundexEntry(J, false, null, null, null, new String[] { Y, DZH }));
    entrySet.put(KS, new DaitchMokotoffSoundexEntry(KS, false, _5, _54, _54, null));
    entrySet.put(KH, new DaitchMokotoffSoundexEntry(KH, false, _5, _5, _5, null));
    entrySet.put(K, new DaitchMokotoffSoundexEntry(K, false, _5, _5, _5, null));
    entrySet.put(L, new DaitchMokotoffSoundexEntry(L, false, _8, _8, _8, null));
    entrySet.put(MN, new DaitchMokotoffSoundexEntry(MN, false, _NC, _66, _66, null));
    entrySet.put(M, new DaitchMokotoffSoundexEntry(M, false, _6, _6, _6, null));
    entrySet.put(NM, new DaitchMokotoffSoundexEntry(NM, false, _NC, _66, _66, null));
    entrySet.put(N, new DaitchMokotoffSoundexEntry(N, false, _6, _6, _6, null));
    entrySet.put(OI, new DaitchMokotoffSoundexEntry(OI, true, _0, _1, _NC, null));
    entrySet.put(OJ, new DaitchMokotoffSoundexEntry(OJ, true, _0, _1, _NC, null));
    entrySet.put(OY, new DaitchMokotoffSoundexEntry(OY, true, _0, _1, _NC, null));
    entrySet.put(O, new DaitchMokotoffSoundexEntry(O, true, _0, _NC, _NC, null));
    entrySet.put(P, new DaitchMokotoffSoundexEntry(P, false, _7, _7, _7, null));
    entrySet.put(PF, new DaitchMokotoffSoundexEntry(PF, false, _7, _7, _7, null));
    entrySet.put(PH, new DaitchMokotoffSoundexEntry(PH, false, _7, _7, _7, null));
    entrySet.put(Q, new DaitchMokotoffSoundexEntry(Q, false, _5, _5, _5, null));
    entrySet.put(RTZ, new DaitchMokotoffSoundexEntry(RTZ, false, _94, _94, _94, null));
    entrySet.put(RZ, new DaitchMokotoffSoundexEntry(RZ, false, null, null, null, new String[] { RTZ, ZH }));
    entrySet.put(RS, new DaitchMokotoffSoundexEntry(RS, false,null, null, null, new String[] { RTZ, ZH }));
    entrySet.put(R, new DaitchMokotoffSoundexEntry(R, false, _9, _9, _9, null));
    entrySet.put(SCHTSCH, new DaitchMokotoffSoundexEntry(SCHTSCH, false, _2, _4, _4, null));
    entrySet.put(SCHTSH, new DaitchMokotoffSoundexEntry(SCHTSH, false, _2, _4, _4, null));
    entrySet.put(SCHTCH, new DaitchMokotoffSoundexEntry(SCHTCH, false, _2, _4, _4, null));
    entrySet.put(SCH, new DaitchMokotoffSoundexEntry(SCH, false, _4, _4, _4, null));
    entrySet.put(SHTCH, new DaitchMokotoffSoundexEntry(SHTCH, false, _2, _4, _4, null));
    entrySet.put(SHCH, new DaitchMokotoffSoundexEntry(SHCH, false, _2, _4, _4, null));
    entrySet.put(SHTSH, new DaitchMokotoffSoundexEntry(SHTSH, false, _2, _4, _4, null));
    entrySet.put(SHT, new DaitchMokotoffSoundexEntry(SHT, false, _2, _43, _43, null));
    entrySet.put(SCHT, new DaitchMokotoffSoundexEntry(SCHT, false, _2, _43, _43, null));
    entrySet.put(SCHD, new DaitchMokotoffSoundexEntry(SCHD, false, _2, _43, _43, null));
    entrySet.put(SH, new DaitchMokotoffSoundexEntry(SH, false, _4, _4, _4, null));
    entrySet.put(STCH, new DaitchMokotoffSoundexEntry(STCH, false, _2, _4, _4, null));
    entrySet.put(STSCH, new DaitchMokotoffSoundexEntry(STSCH, false, _2, _4, _4, null));
    entrySet.put(SC, new DaitchMokotoffSoundexEntry(SC, false, _2, _4, _4, null));
    entrySet.put(STRZ, new DaitchMokotoffSoundexEntry(STRZ, false, _2, _4, _4, null));
    entrySet.put(STRS, new DaitchMokotoffSoundexEntry(STRS, false, _2, _4, _4, null));
    entrySet.put(STSH, new DaitchMokotoffSoundexEntry(STSH, false, _2, _4, _4, null));
    entrySet.put(ST, new DaitchMokotoffSoundexEntry(ST, false, _2, _43, _43, null));
    entrySet.put(SZCZ, new DaitchMokotoffSoundexEntry(SZCZ, false, _2, _4, _4, null));
    entrySet.put(SZCS, new DaitchMokotoffSoundexEntry(SZCS, false, _2, _4, _4, null));
    entrySet.put(SZT, new DaitchMokotoffSoundexEntry(SZT, false, _2, _43, _43, null));
    entrySet.put(SHD, new DaitchMokotoffSoundexEntry(SHD, false, _2, _43, _43, null));
    entrySet.put(SZD, new DaitchMokotoffSoundexEntry(SZD, false, _2, _43, _43, null));
    entrySet.put(SD, new DaitchMokotoffSoundexEntry(SD, false, _2, _43, _43, null));
    entrySet.put(SZ, new DaitchMokotoffSoundexEntry(SZ, false, _4, _4, _4, null));
    entrySet.put(S, new DaitchMokotoffSoundexEntry(S, false, _4, _4, _4, null));
    entrySet.put(TCH, new DaitchMokotoffSoundexEntry(TCH, false, _4, _4, _4, null));
    entrySet.put(TTCH, new DaitchMokotoffSoundexEntry(TTCH, false, _4, _4, _4, null));
    entrySet.put(TTSCH, new DaitchMokotoffSoundexEntry(TTSCH, false, _4, _4, _4, null));
    entrySet.put(THS, new DaitchMokotoffSoundexEntry(THS, false, _4, _4, _4, null));
    entrySet.put(TH, new DaitchMokotoffSoundexEntry(TH, false, _3, _3, _3, null));
    entrySet.put(TRZ, new DaitchMokotoffSoundexEntry(TRZ, false, _4, _4, _4, null));
    entrySet.put(TRS, new DaitchMokotoffSoundexEntry(TRS, false, _4, _4, _4, null));
    entrySet.put(TSCH, new DaitchMokotoffSoundexEntry(TSCH, false, _4, _4, _4, null));
    entrySet.put(TSH, new DaitchMokotoffSoundexEntry(TSH, false, _4, _4, _4, null));
    entrySet.put(TS, new DaitchMokotoffSoundexEntry(TS, false, _4, _4, _4, null));
    entrySet.put(TSK, new DaitchMokotoffSoundexEntry(TSK, false, _45, _45, _45, null));
    entrySet.put(TTS, new DaitchMokotoffSoundexEntry(TTS, false, _4, _4, _4, null));
    entrySet.put(TTSZ, new DaitchMokotoffSoundexEntry(TTSZ, false, _4, _4, _4, null));
    entrySet.put(TC, new DaitchMokotoffSoundexEntry(TC, false, _4, _4, _4, null));
    entrySet.put(TZ, new DaitchMokotoffSoundexEntry(TZ, false, _4, _4, _4, null));
    entrySet.put(TTZ, new DaitchMokotoffSoundexEntry(TTZ, false, _4, _4, _4, null));
    entrySet.put(TZS, new DaitchMokotoffSoundexEntry(TZS, false, _4, _4, _4, null));
    entrySet.put(TSZ, new DaitchMokotoffSoundexEntry(TSZ, false, _4, _4, _4, null));
    entrySet.put(T, new DaitchMokotoffSoundexEntry(T, false, _3, _3, _3, null));
    entrySet.put(UI, new DaitchMokotoffSoundexEntry(UI, true, _0, _1, _NC, null));
    entrySet.put(UJ, new DaitchMokotoffSoundexEntry(UJ, true, _0, _1, _NC, null));
    entrySet.put(UY, new DaitchMokotoffSoundexEntry(UY, true, _0, _1, _NC, null));
    entrySet.put(U, new DaitchMokotoffSoundexEntry(U, true, _0, _NC, _NC, null));
    entrySet.put(UE, new DaitchMokotoffSoundexEntry(UE, true, _0, _NC, _NC, null));
    entrySet.put(V, new DaitchMokotoffSoundexEntry(V, false, _7, _7, _7, null));
    entrySet.put(W, new DaitchMokotoffSoundexEntry(W, false, _7, _7, _7, null));
    entrySet.put(X, new DaitchMokotoffSoundexEntry(X, false, _5, _54, _54, null));
    entrySet.put(Y, new DaitchMokotoffSoundexEntry(Y, true, _1, _NC, _NC, null));
    entrySet.put(ZH, new DaitchMokotoffSoundexEntry(ZH, false, _4, _4, _4, null));
    entrySet.put(ZS, new DaitchMokotoffSoundexEntry(ZS, false, _4, _4, _4, null));
    entrySet.put(ZSH, new DaitchMokotoffSoundexEntry(ZSH, false, _4, _4, _4, null));
    entrySet.put(ZHDZH, new DaitchMokotoffSoundexEntry(ZHDZH, false, _2, _4, _4, null));
    entrySet.put(ZDZ, new DaitchMokotoffSoundexEntry(ZDZ, false, _2, _4, _4, null));
    entrySet.put(ZDZH, new DaitchMokotoffSoundexEntry(ZDZH, false, _2, _4, _4, null));
    entrySet.put(ZD, new DaitchMokotoffSoundexEntry(ZD, false, _2, _43, _43, null));
    entrySet.put(ZHD, new DaitchMokotoffSoundexEntry(ZHD, false, _2, _43, _43, null));
    entrySet.put(ZSCH, new DaitchMokotoffSoundexEntry(ZSCH, false, _4, _4, _4, null));
    entrySet.put(Z, new DaitchMokotoffSoundexEntry(Z, false, _4, _4, _4, null));
    
    startsWithMap.put('a', new String[] { AI, AJ, AY, AU, A });
    startsWithMap.put('b', new String[] { B });
    startsWithMap.put('c', new String[] { CZS, CZ, CSZ, CS, CK, CHS, CH, C });
    startsWithMap.put('d', new String[] { DZS, DZH, DZ, DT, DSZ, DSH, DS, DRZ, DRS, D });
    startsWithMap.put('e', new String[] { EI, EJ, EY, EU, E });
    startsWithMap.put('f', new String[] { FB, F });
    startsWithMap.put('g', new String[] { G });
    startsWithMap.put('h', new String[] { H });
    startsWithMap.put('i', new String[] { IU, IO, IE, IA, I });
    startsWithMap.put('j', new String[] { J });
    startsWithMap.put('k', new String[] { KS, KH, K });
    startsWithMap.put('l', new String[] { L });
    startsWithMap.put('m', new String[] { MN, M });
    startsWithMap.put('n', new String[] { NM, N });
    startsWithMap.put('o', new String[] { OI, OJ, OY, O });
    startsWithMap.put('p', new String[] { PF, PH, P });
    startsWithMap.put('q', new String[] { Q });
    startsWithMap.put('r', new String[] { RTZ, RZ, RS, R });
    startsWithMap.put('s', new String[] { SZT, SZD, SZCZ, SZCS, SZ, STSH, STSCH, STRZ, STRS, STCH, ST, SHTSH, SHTCH, SHT, SHD, SHCH, SH, SD, SCHTSH, SCHTSCH, SCHTCH, SCHT, SCHD, SCH, SC, S });
    startsWithMap.put('t', new String[] { TZS, TZ, TTZ, TTSZ, TTSCH, TTS, TTCH, TSZ, TSK, TSH, TSCH, TS, TRZ, TRS, THS, TH, TCH, TC, T });
    startsWithMap.put('u', new String[] { UI, UJ, UY, UE, U });
    startsWithMap.put('v', new String[] { V });
    startsWithMap.put('w', new String[] { W });
    startsWithMap.put('x', new String[] { X });
    startsWithMap.put('y', new String[] { Y });
    startsWithMap.put('z', new String[] { ZSH, ZSCH, ZS, ZHDZH, ZHD, ZH, ZDZH, ZDZ, ZD, Z });
  }

  public static String[] getStartsWith(char ch)
  {
    return startsWithMap.get(ch);
  }

  public static DaitchMokotoffSoundexEntry getDMSoundexEntry(String prefix)
  {
    return entrySet.get(prefix);
  }



  public String getName()
  {
    return name;
  }

  public String getFirst()
  {
    return first;
  }

  public boolean isVowel()
  {
    return vowel;
  }

  public String getBeforeVowel()
  {
    return before;
  }

  public String getOther()
  {
    return other;
  }

  public String[] getAlternate()
  {
    return alternate;
  }

  public int length()
  {
    return name.length();
  }

  @Override
  public String toString()
  {
    return String.format("{%s, %b, %s, %s, %s, %s}", name, vowel, first, before, other, Arrays.toString(alternate));
  }
  
  public boolean hasAlternate()
  {
    return alternate!=null;
  }
  
}
