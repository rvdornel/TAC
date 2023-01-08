package tac;

import java.util.ArrayList;
import java.util.Map;

public class TacProgram2 {

	private ArrayList<TacInstruction2> lista;
	public ArrayList<String> fonte;
	
	public Map<String, Integer> variables;
	public Map<String, Integer> labels;

    public TacProgram2(){
		lista = new ArrayList<TacInstruction2>();
		fonte = new ArrayList<String>();
		
	}
	
	public ArrayList<String> getFonte()
	{
		return fonte;
	}
	
	public int size() {
		return lista.size();
	}

	public TacInstruction2 get(int pc) {
		return lista.get(pc);
	}
	
	public void limpa(){
		lista.clear();
	}
	
	public void putLinhaFonte(String linha)
	{
		fonte.add(linha);
	}
	
	public void put(TacInstruction2 e){
		lista.add(e);
	}

}


