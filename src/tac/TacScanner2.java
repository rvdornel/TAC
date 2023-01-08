package tac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TacScanner2 {

private String nomearq;
public TacScanner2(String sourceFile) {
	nomearq=sourceFile;
}

public TacProgram2 scan(TacExecutionEngine x) {
    try {
        FileReader arq = new FileReader(nomearq);
        BufferedReader lerArq = new BufferedReader(arq);
        int numlin=1;
        String linha = lerArq.readLine(); // lê a primeira linha
  // a variável "linha" recebe o valor "null" quando o processo
  // de repetição atingir o final do arquivo texto
        while (linha != null) {
          System.out.printf("%s\n", linha);
          if (linha!="")
        	  {
        	  TacExecutionEngine.program.putLinhaFonte(linha);
          TacInstruction2 inst=new TacInstruction2(TacExecutionEngine.program,linha,numlin);
         // inst.putKind(inst.InstructionKind.GOTO);
          
          TacExecutionEngine.program.put(inst);
         	  }
             linha = lerArq.readLine(); // lê da segunda até a última linha
          numlin++;
        }
   
        arq.close();
      } catch (IOException e) {
          System.err.printf("Erro na abertura do arquivo: %s.\n",
            e.getMessage());
      }
   
      System.out.println();
   
 
return TacExecutionEngine.program;
}

}

