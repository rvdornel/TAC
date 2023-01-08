package tac;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.table.AbstractTableModel;

public class ArrayListModel extends AbstractTableModel {  
	   
    private final List<String> lista = new ArrayList<>();  
    private static final long serialVersionUID = 1L;    
    public int size() {  
        return lista.size();  
    }  
  
    public boolean add(String e) {  
        boolean result = lista.add(e);  
        if (result) {  
            int index = lista.size() - 1;  
            fireTableRowsInserted(index, index);  
        }  
        return result;  
    }  
  
    public boolean remove(Object o) {  
        boolean result = lista.remove(o);  
        if (result) {  
            int index = lista.size();  
            fireTableRowsDeleted(index, index);  
        }  
        return result;  
    }  
  
    public void clear() {  
        int index = lista.size() - 1;  
        lista.clear();  
        fireTableRowsDeleted(0, index);  
    }  
  
    public String get(int index) {  
        return lista.get(index);  
    }  
  
    public void add(int index, String element) {  
        lista.add(index, element);  
        fireTableRowsInserted(index, index);  
    }  
  
    public String remove(int index) {  
        String obj = lista.remove(index);  
        fireTableRowsDeleted(index, index);  
        return obj;  
    }  
  
    @Override  
    public int getRowCount() {  
        return lista.size();  
    }  
  
    @Override  
    public int getColumnCount() {  
        return 1;  
    }  
  
    @Override  
    public Object getValueAt(int rowIndex, int columnIndex) {  
        if (columnIndex != 0)  
            throw new NoSuchElementException("coluna inválida: " + columnIndex);  
        return lista.get(rowIndex);  
    }  
}  


