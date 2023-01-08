package tac;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class TacExecutionEngine
{

	   private static int delay=2000;
	   private static String sourceFile="";
	   private static String sourceAnt="";
	   private static boolean pausar;
	   private static boolean zerar;
 	   public static TacProgram2 program;
	   private static JTable table2;
	   private static JTable tabela_programa;
	   private static JTable tabela_pilha;
	   private static JFrame teste;
	   private static JTextArea tempo;
	   private static DefaultTableModel modelo2;	
	   private static DefaultTableModel modelo;
	   private static int vPilha[];
	   private static DefaultTableModel pilha;
	   private static JTextArea areaEscrita;
	   private static int BP=0;
	   private static int SP=120;
	   private static int pc=1;
	      	      	
	public TacExecutionEngine(String sourceFile)
   { 
   vPilha = new int[31];
   program = new TacProgram2();
      /* Initialize other data structures here */
   program.variables = new HashMap<String,Integer>();
   
   program.labels = new HashMap<String,Integer>(); 
   
   TacScanner2 scanner2=new TacScanner2(sourceFile);      
   program = scanner2.scan(this);
   System.out.println("*** Scanning complete; execution commencing ***");
   program.variables.put("_SP",SP);  
   program.variables.put("_BP",BP);  
   }

	private static void zera_variaveis()
	{
        for (Map.Entry <String, Integer> entry : program.variables.entrySet())
            entry.setValue(0);
    }
	
	private static void atualiza_tabela(int pc)
	{
        int i=0;
        modelo2.setValueAt(pc,0,0);
        
        for (Map.Entry <String, Integer> entry : program.variables.entrySet())
        {
        	System.out.println(entry.getKey());
            modelo2.setValueAt(entry.getValue(),0,i+1);
            i++;
        }   
        	
	}
private static void seta_pc(Integer val)
{
    modelo.setValueAt("",pc, 0);        
	pc=val;
    modelo.setValueAt("-->",pc, 0);        
}
	
	
   /**
    *  Execute this program.
    *  Makes an inital pass to process label locations,
    *  and a secondary pass to execute the code.
    */
	
static public int passo()
{
 //   try{Thread.sleep(delay);}
//	 catch(InterruptedException e){System.out.println(e);}  
	 TacInstruction2 currentInstruction = program.get(pc);
  int kind = currentInstruction.getKind();
  if( kind==TacInstruction2.InstructionKind.LABEL ) seta_pc(pc+1);
  else if( kind==TacInstruction2.InstructionKind.THREEADDR ){
     String x = currentInstruction.getAddr1();
     int y = parseIntOrGet( currentInstruction.getAddr2() );
     int z = parseIntOrGet( currentInstruction.getAddr3() );
     System.out.println(x+" y="+y+" z="+z);
     Integer result=0;
     switch( currentInstruction.getOp() ){
       // ARITHMETIC OPERATORS
       case "+": result=y+z; break;
       case "-": result=y-z; break;
       case "/": result=y/z; break;
       case "*": result=y*z; break;
       case "%": result=y%z; break;
       case "&": result=y&z; break;
       case "|": result=y|z; break;
       case ">": if( y > z) result=1; else result=0; break;
       case ">=": if( y >= z) result=1; else result=0; break;
       case "<": if( y < z) result=1; else result=0; break;
       case "<=": if( y <= z) result=1; else result=0; break;
       case "==": if( y == z) result=1; else result=0; break;
       case "<>": if( y != z) result=1; else result=0; break;
       case "&&": if( y!=0 && z!=0) result=1; else result=0; break;
       case "||": if( y!=0 || z!=0) result=1; else result=0; break;
       default:
           System.err.println("Symbol not recognised");
           System.exit(1);
       }
     put_Addr1(x, result); 
     seta_pc(pc+1);
    } else if( kind==TacInstruction2.InstructionKind.CONDJUMP ){
    	seta_pc(pc+1);
    	int x = parseIntOrGet( currentInstruction.getAddr1() );
        int y = parseIntOrGet( currentInstruction.getAddr2() );
        switch( currentInstruction.getOp() ){
       // BOOLEAN OPERATORS
       // Assigning value 1 for true and 0 for false
       case ">":
         if( x > y) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
         catch(NullPointerException e){
        	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
        	 System.exit(0);}
         break;
       case ">=":
           if( x >= y) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
           catch(NullPointerException e){
          	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
          	 System.exit(0);}
           break;
       case "<":
           if( x < y) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
           catch(NullPointerException e){
          	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
          	 System.exit(0);}
           break;
       case "<=":
           if( x <= y) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
           catch(NullPointerException e){
          	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
          	 System.exit(0);}
           break;
       case "==":
           if( x == y) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
           catch(NullPointerException e){
          	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
          	 System.exit(0);}
           break;
       case "<>":
           if( x != y) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
           catch(NullPointerException e){
          	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
          	 System.exit(0);}
           break;
       case "&&":
           if ( x!=0 && y!=0) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
           catch(NullPointerException e){
          	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
          	 System.exit(0);}
           break;
       case "||":
           if ( x!=0 || y!=0) try{seta_pc(program.labels.get( currentInstruction.getLabel() ));}
           catch(NullPointerException e){
          	 JOptionPane.showMessageDialog(null, "Label "+currentInstruction.getLabel()+" não declarado");
          	 System.exit(0);}
           break;
       default:
           System.err.println("Symbol not recognised");
           System.exit(1);
     }

  } else if( kind==TacInstruction2.InstructionKind.TWOADDR ){
     // TWOADDR :: x := y; | Assignment 
     String x = currentInstruction.getAddr1();
     String sy = currentInstruction.getAddr2();
     int y = parseIntOrGet(sy);
     if (currentInstruction.getOp() =="-") y=-y;
     put_Addr1(x, y);
     seta_pc(pc+1);
  } else if( kind==TacInstruction2.InstructionKind.GOTO){
     // GOTO :: goto label; | Program jump
     seta_pc(program.labels.get( currentInstruction.getLabel()));
  } else if( kind==TacInstruction2.InstructionKind.CALL){
      pilha.setValueAt("", SP/4, 2);
      pilha.setValueAt(pc+1, SP/4, 1);
    	 vPilha[SP/4]=pc+1;
    	 SP-=4;
    	 program.variables.put("_SP",SP);  
      pilha.setValueAt("<-SP", SP/4, 2);
    	 seta_pc(program.labels.get( currentInstruction.getLabel()));
  } else if( kind==TacInstruction2.InstructionKind.PUSH){
      pilha.setValueAt("", SP/4, 2);
      int y = parseIntOrGet( currentInstruction.getAddr1() );
      vPilha[SP/4]=y;
      pilha.setValueAt(y, SP/4, 1);
      SP-=4;
      program.variables.put("_SP",SP);  
      pilha.setValueAt("<-SP", SP/4, 2);
      seta_pc(pc+1);
        } else if( kind==TacInstruction2.InstructionKind.RETURN){
      pilha.setValueAt("", SP/4, 2);
      SP+=4;
      program.variables.put("_SP",SP);  
      seta_pc(vPilha[SP/4]);
      pilha.setValueAt("<-SP", SP/4, 2);
  } else if( kind==TacInstruction2.InstructionKind.POP){
      pilha.setValueAt("", SP/4, 2);
      String x = currentInstruction.getAddr1();
      SP+=4;
      program.variables.put("_SP",SP);  
      int y = vPilha[SP/4];
      put_Addr1(x, y);
      pilha.setValueAt("<-SP", SP/4, 2);
      seta_pc(pc+1);
  } else if( kind==TacInstruction2.InstructionKind.READ) {
     // READ :: read x; | Asks user for input
 	 String nome = null;
 	 while (nome == null || nome.equals("")) {
 	    nome = JOptionPane.showInputDialog("Entre com o valor da variável "+currentInstruction.getAddr1());
 	    if (nome == null || nome.equals("")) {
 	       JOptionPane.showMessageDialog(null,"Tente de novo");
 	       }
 	    }
 	  int variableRead = Integer.parseInt(nome);
       program.variables.put( currentInstruction.getAddr1(),variableRead );
       seta_pc(pc+1);
         } else if( kind==TacInstruction2.InstructionKind.WRITE){
     // WRITE :: write x; | Prints a variable to screen
     areaEscrita.append(""+parseIntOrGet( currentInstruction.getAddr1())+"\n" );
     seta_pc(pc+1);
       } else if( kind==TacInstruction2.InstructionKind.HALT){
     // HALT :: halt; | Quits program
     return -1;
  } 
  atualiza_tabela(pc);
return 0;
}

	
   public void executa()
   {
      // Put the line numbers of each label in a map of labels to
      // line numbers...
	  System.out.println("Vou executar "+program.size()+" instruções");
      for(int i = 0; i < program.size();i++ ){
    	  System.out.println("Vou executar a instrução "+i);
         TacInstruction2 currentInstruction = program.get(i);
         if( currentInstruction.getKind()==TacInstruction2.InstructionKind.LABEL){
            program.labels.put( currentInstruction.getLabel(), i );
         }
      }
      seta_pc(0);
      // Executing the program line by line, using a program counter
      // that can be modified from within the loop by jump instructions.
      for(; pc < program.size();){
          if (zerar){zerar=false;pc=-1;zera_variaveis();areaEscrita.setText(null);continue;}
          while (pausar);
//        	  if (passo()==-1) return;
      }
      // If program has reached here, it's exited without reaching
      // a HALT instruction.
      System.err.println("No HALT instruction reached at EOF.");
    //  System.exit(1);

   }

   public static void put_Addr1(String s, Integer val)
   {
	   if (s.contains("_BP"))
   	 {
  	 if (s.contains("[")==false)
  	    {
  		BP=val;
  	    program.variables.put("_BP",BP);  
	  	return;
  		}
  	 Integer desloc=0;
  	 if (s.contains("+"))
  		 {
		 s=s.substring(s.lastIndexOf("-") + 1);
		 s=s.replaceAll("[\\D]", "");
  		 desloc=Integer.parseInt(s);
  		 }
  	 if (s.contains("-"))
   		 {
		 s=s.substring(s.lastIndexOf("+") + 1);
		 s=s.replaceAll("[\\D]", "");
		     desloc=-Integer.parseInt(s);
		     }
  	 vPilha[(BP+desloc)/4]=val;
  	 pilha.setValueAt(val, (BP+desloc)/4,1);
     
  	 return;
   	 }
	   if (s.contains("_SP"))
	   	 {
	  	 if (s.contains("[")==false) 
	  	    {
	  	    pilha.setValueAt("", SP/4, 2);
	  	    SP=val; 
	  	    pilha.setValueAt("<- SP", SP/4, 2);
	  	    program.variables.put("_SP",SP);  
	  	    return;
	  	    }
	  	 Integer desloc=0;
	  	 if (s.contains("+"))
	  		 {
    		 s=s.substring(s.lastIndexOf("+") + 1);
    		 s=s.replaceAll("[\\D]", "");
	  		 desloc=Integer.parseInt(s);
	  		 }
	  	 if (s.contains("-"))
	   		 {
    		 s=s.substring(s.lastIndexOf("-") + 1);
    		 s=s.replaceAll("[\\D]", "");
			     desloc=-Integer.parseInt(s);
			     }
	  	 vPilha[(SP+desloc)/4]=val;
	  	 pilha.setValueAt(val, (SP+desloc)/4,1);
	  	 return;
	   	 }
	   program.variables.put(s,val);
	       	   
   }
   
   /**
    * Method that parses the "address" format in TAC returning an integer
    * from either a parsed integer string, or the integer that is
    * represented by that string in the variables map.
    *
    * If this method can not parse an integer or find a variable,
    * it will quit the program with an error message.
    * 
    * @param  s String to parse\lookup.
    * @return   An integer value.
    */
   public static int parseIntOrGet(String s){
      Integer returnVal;
      if (s.contains("_BP"))
     	 {
    	 if (s.contains("[")==false) return BP;
    	 Integer desloc=0;
    	 if (s.contains("+"))
    		 {
    		 s=s.substring(s.lastIndexOf("+") + 1);
    		 s=s.replaceAll("[\\D]", "");
 	    	 desloc=Integer.parseInt(s);
    		 }
    	 if (s.contains("-"))
     		 {
    		 s=s.substring(s.lastIndexOf("-") + 1);
    		 s=s.replaceAll("[\\D]", "");
 	    	 desloc=-Integer.parseInt(s);
		     }
    	 returnVal=vPilha[(BP+desloc)/4];
    	 return returnVal;
     	 }
      if (s.contains("_SP"))
  	 {
     if (s.contains("[")==false) return SP;
 	 Integer desloc=0;
 	 if (s.contains("+"))
 		 {
 		 s=s.substring(s.lastIndexOf("+") + 1);
 		 desloc=Integer.parseInt(s);
 		 }
 	 if (s.contains("-"))
  		 {
	    	 s=s.substring(s.lastIndexOf("+") + 1);
		     desloc=-Integer.parseInt(s);
		     }
 	 returnVal=vPilha[(SP+desloc)/4];
 	 return returnVal;
  	 }
   try {
         returnVal = Integer.parseInt(s);
      } catch (NumberFormatException e) {
    	 returnVal = program.variables.get(s);
         if(returnVal == null){
            System.err.println("Variable " + s + " not initialised properly.");
            System.exit(1);
         }
      }

      return returnVal;
   }

   private static void carrega_programa()
   {
  		sourceFile="";
  		JFileChooser chooser = new JFileChooser();
  		chooser.setCurrentDirectory(new java.io.File("."));
  		chooser.setDialogTitle("choosertitle");
//  		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  		chooser.setAcceptAllFileFilterUsed(false);

  		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
  		  System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
  		  System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
  		  sourceFile=chooser.getCurrentDirectory()+"\\"+chooser.getSelectedFile().getName();
  		} else {
  		  System.out.println("No Selection ");
  		}
       /* Create execution environment and execute program */
   }
   
      /* The instructions comprising this program */
   public static void main(String args[])
   {  System.out.println("TacExecutionEngine v1.0: KTH 11/11/'11");
      System.out.println("Beta version");
/*      if (args.length < 1 )
      {  System.err.println("Usage: java TacExecutionEngine filename");
         System.exit(1);
      } 
      String sourceFile = args[0];*/
      
      JButton jbnButton1,jbnButton2,jbnButton3,
              jbnButton4,jbnButton5,jbnButton6,jbnButton7;
  	  JPanel jplPanel;
  	  JScrollPane barraRolagem; // área do programa
      JScrollPane barraRolagem2; // tabela de variáveis
      JScrollPane barraRolagem3; // pilha
      
      
        // Botões        
        
        jbnButton1 = new JButton("Reset");
  		jbnButton2 = new JButton("Sair");
 		jbnButton3 = new JButton("Carregar");
 		jbnButton4 = new JButton("Mais devagar!");
 		jbnButton5 = new JButton("Mais rápido");
 		jbnButton6 = new JButton("De novo!!!");
 		jbnButton7 = new JButton("Passo");
 		
 		jplPanel = new JPanel();
  		jbnButton1.setMnemonic(KeyEvent.VK_I); //Set ShortCut Keys
  		jbnButton1.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				zerar=true;
  		      for (Map.Entry <String, Integer> entry : TacExecutionEngine.program.variables.entrySet())
  		    	TacExecutionEngine.program.variables.put(entry.getKey(),0);
              SP=120;
  		      BP=0;
  		      program.variables.put("_SP",SP);  
  		      program.variables.put("_BP",BP);  
              seta_pc(0);
  		      atualiza_tabela(pc);
		      for (Integer i=0;i<=30;i++)
  		    	  {
  		    	  vPilha[i]=0;
  		          pilha.setValueAt("", i, 2);
  		          pilha.setValueAt("", i, 1);
  		    	  }
		      pilha.setValueAt("<-SP", 30, 2);
		      areaEscrita.setText(null);
  		   }
  		});
  		jbnButton2.setMnemonic(KeyEvent.VK_I);
  		jbnButton2.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				System.exit(1);
  			}
  		});
  		jbnButton3.setMnemonic(KeyEvent.VK_I);
  		jbnButton3.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				carrega_programa();
  			}
  		});
  		jbnButton4.setMnemonic(KeyEvent.VK_I);
  		jbnButton4.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				delay+=100;
  				tempo.setText(""+delay);
  		  		
  			}
  		});
  		jbnButton5.setMnemonic(KeyEvent.VK_I);
  		jbnButton5.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				delay-=100;
  				tempo.setText(""+delay);
  		  			}
  		});
  		jbnButton6.setMnemonic(KeyEvent.VK_I);
  		jbnButton6.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				sourceFile=sourceAnt;	}
  		});
  		jbnButton7.setMnemonic(KeyEvent.VK_I);
  		jbnButton7.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				passo();}
  		});
  		jplPanel.add(jbnButton1);
  		jplPanel.add(jbnButton2);
  		jplPanel.add(jbnButton3);
  		jplPanel.add(jbnButton4);
  		jplPanel.add(jbnButton5);
  		jplPanel.add(jbnButton6);
  		jplPanel.add(jbnButton7);
  	  	tempo=new JTextArea();
  		tempo.setText(""+delay);
  		jplPanel.add(tempo);
  		jplPanel.setPreferredSize(new Dimension(300, 100));
  		jplPanel.setMaximumSize(jplPanel.getPreferredSize()); 
  		jplPanel.setMinimumSize(jplPanel.getPreferredSize());
// Caixa de seleção do nome do arquivo
  		
  		 teste=new JFrame();
  	      teste.setSize(1000, 700);
  	      teste.add(jplPanel);	
  	    teste.setVisible(true);
  	                

      TacExecutionEngine engine=null;
      System.out.println("Chegay 1");
      while (true){
    	  
      if (sourceFile.equalsIgnoreCase("")==false)
    	  {
    	     modelo = new DefaultTableModel();
    	     tabela_programa = new JTable(modelo);
    	     modelo.addColumn("PC");
    	     modelo.addColumn("Número");
    	     modelo.addColumn("Instrução");
             engine = new TacExecutionEngine(sourceFile);
		     for (int i=0;i<TacExecutionEngine.program.fonte.size();i++)
		    	  modelo.addRow(new Object[] { "",i,TacExecutionEngine.program.fonte.get(i)});
		   
		      Vector<String> nomes = new Vector<String>();
		      nomes.addElement(new String("pc"));
		 		   for (Map.Entry <String, Integer> entry : TacExecutionEngine.program.variables.entrySet())
		      nomes.addElement(new String(entry.getKey()));
		 		// Area do Programa
		 	      
		 	      JPanel painelTab2 = new JPanel();
		 	      painelTab2.setLayout(new GridLayout(1, 1));
		 	      barraRolagem = new JScrollPane(tabela_programa);
		 	      painelTab2.add(barraRolagem); 
		 	     
		 	      // Area de Escritas      
		 	      
		 	      areaEscrita = new JTextArea(5, 20);
		 	      JScrollPane scrollPane = new JScrollPane(areaEscrita); 
		 	      areaEscrita.setEditable(false); 
		 	      painelTab2.add(scrollPane); 
		 	     
		 	      // Pilha
		 	      
		 	      pilha = new DefaultTableModel();
	    	      tabela_pilha = new JTable(pilha);
	    	      pilha.addColumn("End");
	    	      pilha.addColumn("Pilha");
	    	      pilha.addColumn("_SP _BP");
	              for (int i=0;i<=30;i++)
			    	  pilha.addRow(new Object[] {i*4,"",""});
			      JPanel painelPilha = new JPanel();
		 	      painelPilha.setLayout(new GridLayout(1, 1));
		 	      barraRolagem3 = new JScrollPane(tabela_pilha);
		 	      painelTab2.add(barraRolagem3); 
		     	  pilha.setValueAt("<-SP", 30, 2);
		         
		 	     
      
      
// Area de variaveis     
      
      TacExecutionEngine.modelo2 = new DefaultTableModel(nomes, 1);
      table2 = new JTable(TacExecutionEngine.modelo2); 
      atualiza_tabela(0);
      barraRolagem2 = new JScrollPane(table2);
      painelTab2.add(barraRolagem2); 
      JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
      split.setTopComponent(jplPanel);
      split.setBottomComponent(painelTab2);
      split.setDividerLocation(0.9);
      teste.dispose();
      teste=new JFrame();
      teste.setSize(1000, 700);
      teste.setVisible(true);
      teste.getContentPane().add(split);
      teste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      teste.setVisible(true);
		engine.executa();
		sourceAnt=sourceFile;
    	sourceFile="";   
        System.out.println("*** Execution complete                      ***");
      }
      }
   }
}
