import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.lang.IndexOutOfBoundsException;
import java.lang.Math;

/* NodeData
-creates a class that stores 3 variables (row, column, data)
 to be stored in each node in the linked list.
*/
class NodeData{
  private int row;
  private int col;
  private int data;
  public NodeData(int a, int b, int c){
    row = a;
    col = b;
    data = c;
  }
  //Getter methods
  public int getCol(){return col;}
  public int getRow(){return row;}
  public int getData(){return data;}
  //Setter methods
  public void setCol(int newCol){col = newCol;}
  public void setRow(int newRow){row = newRow;}
  public void setData(int newData){data = newData;}
}


public class SparseMatrix implements SparseInterface{
  private int sizeMatrix;
  private LinkedList<NodeData> list;
  public SparseMatrix(){
    sizeMatrix = 5; //initialize size 5
    list = new LinkedList<NodeData>(); //initialize sparsematrix
  }


  //CLEAR - clears all matrix values
  public void clear(){
    list.clear();
  }

  //SETSIZE - clears all matrix values, sets size to input
  public void setSize(int size){
    list.clear();
    sizeMatrix = size;
  }

  //ADDELEMENT
  //- adds element to specified row & column
  //- throws error if row or col are out of bounds
  //- replaces element if same row and col
  public void addElement(int row, int col, int data){
    if (data == 0){
      return; //no point in adding 0 element
    }
    //initialize index and newnode and tempNode
    int location = 0;
    NodeData newNode = new NodeData(row,col,data);
    NodeData tempNode;

    //check if inputs are in bounds
    if (row > getSize()-1 || col > getSize()-1){
      System.out.println("threw: " + newNode.getRow() + " " + newNode.getCol());
      throw new IndexOutOfBoundsException();
    }
    //add elements in order
    for (int i=0; i<list.size(); i++){
      tempNode = list.get(i);
      //check if element location already exists
      if (row == tempNode.getRow() && col == tempNode.getCol()){
        list.set(i,newNode); //replace element
        return;
      }
      //use location to find indice of where newNode should be placed
      if (row > tempNode.getRow()){
        location++;
      }
      if (row == tempNode.getRow()){
        if (col > tempNode.getCol()){
          location++;
        }
      }
    }
    list.add(location, newNode);
  }

  //REMOVEELEMENT
  //- removes element at specified row and column
  //- throw error if row and col does not have an element or if out of bounds
  public void removeElement(int row, int col){
    //check if in bounds
    if (row > getSize()-1 || col > getSize()-1){
      throw new IndexOutOfBoundsException();
    }
    //iterate through to find element and remove
    for (int i=0; i<list.size(); i++){
      NodeData tempNode = list.get(i);
      if (tempNode.getRow() == row && tempNode.getCol() == col){
        list.remove(i);
        return;
      }
    }
    throw new NoSuchElementException();
  }

  //GETELEMENT
  public int getElement(int row, int col){
    //check if in bounds
    if (row > getSize()-1 || col > getSize()-1){
      throw new IndexOutOfBoundsException();
    }
    //iterate through to find element
    for (int i=0; i<list.size(); i++){
      NodeData tempNode = list.get(i);
      if (tempNode.getRow() == row && tempNode.getCol() == col){
        return tempNode.getData();
      }
    }
    //if there is no node found and the row and col is inside
    //the matrix size nxn, then the value is 0.
    if (row < getSize() && col < getSize()){
      return 0;
    }
    throw new NoSuchElementException();
  }

  //DETERMINANT
  public int determinant(){
    //BASE CASE
    int deter=0;
    if (getSize() == 2){
      // return a*d - b*c
      deter = (getElement(0,0)*getElement(1,1))-(getElement(0,1)*getElement(1,0));
      return deter;
    }
    //RECURSIVE CASE
    for (int i=0;i<getSize();i++){
      int tempElement = getElement(0,i);
      deter = (int)Math.pow(-1,i)*tempElement*minor(0,i).determinant()+ deter;
    }
    return deter;
}

  //MINOR
  public SparseInterface minor(int row, int col){
    SparseMatrix newMatrix = new SparseMatrix();
    newMatrix.setSize(getSize()-1);
    NodeData tempNode;
    for (int i=0; i<list.size(); i++){
      tempNode = list.get(i);
      //check to see if element is not in specified row/col being removed.
      if (row != tempNode.getRow() || col != tempNode.getCol()){
        //elements in row/col must rearranged to fit new N-1 matrix
        if (tempNode.getCol() > col && tempNode.getRow() > row){
          newMatrix.addElement(tempNode.getRow()-1,tempNode.getCol()-1,tempNode.getData());
        }
        else if (tempNode.getCol() < col && tempNode.getRow() > row){
          newMatrix.addElement(tempNode.getRow()-1,tempNode.getCol(),tempNode.getData());
        }
      }
    }
    return newMatrix;
  }

  //TOSTRING
  public String toString(){
    //list elements must be sorted before returning string
    String str = "";
    for (int i=0; i < list.size(); i++ ){
      NodeData tempNode = list.get(i);
      str = str + tempNode.getRow() + " " + tempNode.getCol() + " "
      + tempNode.getData() + "\n";
    }
    return str;
  }

  //GETSIZE - returns matrix size
  public int getSize(){
    return sizeMatrix;
  }
}
