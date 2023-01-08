package tac;

import javax.swing.JOptionPane;

public class TacInstruction2 {

	private String Label;
	private String Addr1;
	private String Addr2;
	private String Addr3;
	private String Op;
	private int Tipo;
	
	public class InstructionKind {

		public static final int THREEADDR = 1;
		public static final int READ = 2;
		public static final int CONDJUMP = 3;
		public static final int WRITE = 4;
		public static final int HALT = 5;
		public static final int TWOADDR = 6;
		public static final int GOTO = 7;
		public static final int CALL = 8;
		public static final int LABEL = 9;
		public static final int RETURN = 10;
		public static final int PUSH = 11;
		public static final int POP = 12;

	}

	String linha;
	int pos;
    int numlin;
	
	private String proxAdd()
	{
	    char c=linha.charAt(pos);
	    while (!(c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' || c=='[' || c==']' || c=='_' )) c=linha.charAt(++pos);
        String st="";
        if (c=='['){
        	while (c!=']'){st+=c;pos++;if (pos<linha.length()) c=linha.charAt(pos);}
            st+=c;pos++;
        }
        else
            while (pos<linha.length() && (c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' || c=='[' || c==']'|| c=='_') ) {
                st+=c;pos++;if (pos<linha.length()) c=linha.charAt(pos);}
        return st;
	}
	
	private void pulaAdd()
	{
	    char c=linha.charAt(pos);
	    while (!(c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' || c=='[' || c==']'|| c=='_' )) c=linha.charAt(++pos);
        if (c=='['){
        	while (c!=']'){if (pos<linha.length()) c=linha.charAt(pos);}
        	pos++;
        } 
        else
        	while (pos<linha.length() && (c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' || c=='[' || c==']'|| c=='_') ) {pos++;if (pos<linha.length()) c=linha.charAt(pos);}
        return;
	}

	public void trata_if(TacProgram2 program)
	{
    	pulaAdd(); //pula if
        Tipo=InstructionKind.CONDJUMP;
	    Addr1=proxAdd();
	    if (Addr1.charAt(0)<'0' || Addr1.charAt(0)>'9') if (!Addr1.contains("[")) program.variables.put(Addr1,0);
	    Addr2=proxAdd();
	    if (Addr2.charAt(0)<'0' || Addr2.charAt(0)>'9') if (!Addr2.contains("[")) program.variables.put(Addr2,0);
	    pulaAdd(); //pula goto
        Label=proxAdd();
	    if (linha.contains(">=")) Op=">=";
	    else if (linha.contains("<>")) Op="<>";
	    else if (linha.contains(">")) Op=">";
	    else if (linha.contains("<=")) Op="<=";
	    else if (linha.contains("<")) Op="<";
	    else if (linha.contains("==")) Op="==";
	    else if (linha.contains("&&")) Op="==";
	    else {JOptionPane.showMessageDialog(null, "Comando if com operador inválido na linha "+numlin+":"+linha);
	    		System.exit(0);}
	    }
	
	public void trata_mais_menos(TacProgram2 program)
	{
	pos=0;
	Addr1=proxAdd();if (!Addr1.contains("[")) program.variables.put(Addr1,0);
	char c=linha.charAt(pos);
    while (c!='=') c=linha.charAt(++pos);
    pos++;
    c=linha.charAt(pos);
    while (c==' ') c=linha.charAt(++pos);
    if (c!='-' && c!='+') // é binário
       {
       pos=0;
       trata_three(program);
       return;
       }
    Tipo=InstructionKind.TWOADDR;
    if (c=='+') Op=" ";
    if (c=='-') Op="-";
    Addr2=proxAdd();
	if (Addr2.charAt(0)<'0' || Addr2.charAt(0)>'9') if (!Addr2.contains("[")) program.variables.put(Addr2,0);
	return;
	}
	
	public void trata_three(TacProgram2 program)
	{
        Tipo=InstructionKind.THREEADDR;
	    Addr1=proxAdd();if (!Addr1.contains("[")) program.variables.put(Addr1,0);
	    Addr2=proxAdd();
	    if (Addr2.charAt(0)<'0' || Addr2.charAt(0)>'9') if (!Addr2.contains("[")) program.variables.put(Addr2,0);
	    Addr3=proxAdd();
	    if (Addr3.charAt(0)<'0' || Addr3.charAt(0)>'9')if (!Addr3.contains("[")) program.variables.put(Addr3,0);
		if (linha.contains("+")) Op="+";
	    else if (linha.contains("-")) Op="-";
	    else if (linha.contains("*")) Op="*";
		else if (linha.contains("/")) Op="/";
	    else if (linha.contains("%")) Op="%";
	    else if (linha.contains("&&")) Op="&&";
	    else if (linha.contains("&")) Op="&";
	    else if (linha.contains("||")) Op="||";
	    else if (linha.contains("|")) Op="|";
	    else if (linha.contains(">=")) Op=">=";
	    else if (linha.contains("<>")) Op="<>";
	    else if (linha.contains(">")) Op=">";
	    else if (linha.contains("<=")) Op="<=";
		else if (linha.contains("<")) Op="<";
	    else if (linha.contains("==")) Op="==";
	    else {JOptionPane.showMessageDialog(null, "Atribuição com operador inválido na linha "+numlin+":"+linha);System.exit(0);}
	    }

	public TacInstruction2(TacProgram2 program, String e, int num){
		linha=e;
		numlin=num;
		pos=0;
		System.out.println(linha);
		
		if (linha.contains(":"))
			{
			Label=proxAdd();
            Tipo=InstructionKind.LABEL;
            System.out.println("Label "+Label);
            }
    else if (linha.contains((CharSequence)"write"))
	{
	    pulaAdd(); // pula write
	    Addr1=proxAdd();
	    if (Addr1.charAt(0)<'0' || Addr1.charAt(0)>'9')if (!Addr1.contains("[")) program.variables.put(Addr1,0);
        Tipo=InstructionKind.WRITE;
		System.out.println("É escrita");
        }
    else if (linha.contains((CharSequence)"read"))
	{
	    pulaAdd(); // pula write
	    Addr1=proxAdd();
	    if (Addr1.charAt(0)<'0' || Addr1.charAt(0)>'9')if (!Addr1.contains("[")) program.variables.put(Addr1,0);
        Tipo=InstructionKind.READ;
		System.out.println("É leitura");
        }
    else if (linha.contains("if ")) trata_if(program);
    else if (linha.contains("*") ||
		linha.contains("/") ||
		linha.contains("%") ||
		linha.contains(">=")||
		linha.contains("<>")||
		linha.contains(">") ||
		linha.contains("<=")||
		linha.contains("<") ||
		linha.contains("==")) trata_three(program);
    else if ((linha.contains("+") || linha.contains("-"))&& !linha.contains("["))
    	trata_mais_menos(program);
    else if (linha.contains("=")){
    	   Tipo=InstructionKind.TWOADDR;
    	   Op="";
    	   Addr1=proxAdd();if (!Addr1.contains("[")) program.variables.put(Addr1,0);
    	   Addr2=proxAdd();
    	   if (Addr2.charAt(0)<'0' || Addr2.charAt(0)>'9') if (!Addr2.contains("[")) program.variables.put(Addr2,0);
    	   }
 	else if (linha.contains((CharSequence)"goto"))
    {	pulaAdd();
		Label=proxAdd();
	    Tipo=InstructionKind.GOTO;
    }
	else if (linha.contains((CharSequence)"call"))
    {	pulaAdd();
		Label=proxAdd();
	    Tipo=InstructionKind.CALL;
    }
	else if (linha.contains((CharSequence)"push"))
    {	pulaAdd();
		Addr1=proxAdd();
	    Tipo=InstructionKind.PUSH;
    }
	else if (linha.contains((CharSequence)"pop"))
    {	pulaAdd();
		Addr1=proxAdd();
	    Tipo=InstructionKind.POP;
    }
	else if (linha.contains((CharSequence)"ret"))
    {	Tipo=InstructionKind.RETURN;
    }
    else if (linha.contains("halt"))
    {
        Tipo=InstructionKind.HALT;
		System.out.println("É halt");
    }
    else {JOptionPane.showMessageDialog(null, "Comando não identificado na linha "+numlin+":"+linha);System.exit(0);}

	}

public int getKind() {
		return Tipo;
	}

	public String getLabel() {
		return Label;
	}

	public String getAddr1() {
		return Addr1;
	}

	public String getAddr2() {
		return Addr2;
	}

	public String getAddr3() {
		return Addr3;
	}

	public String getOp() {
		return Op;
	}

		}
/**
*  Create this (initially empty) program.
*/


