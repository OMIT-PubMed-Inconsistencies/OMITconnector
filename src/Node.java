import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nisansa on 16/05/11.
 */
public class Node implements PatternElement  {
    String id="";
    String name="";
    String parent="";
    Node parentNode=null;
    ArrayList<Node> children=new ArrayList<Node>();

    public Node(String id, String name, String parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public void addChild(Node n){
        children.add(n);
        n.parentNode=this;
    }

    public Node findNode(String s){
        if(name.equalsIgnoreCase(s)){
            return this;
        }
        for (int i = 0; i <children.size() ; i++) {
            if(children.get(i).findString(s)){
                return children.get(i).findNode(s);
            }
        }
        return null;
    }

    public boolean findString(String s){
        String s1="";
        StringBuilder sb;
        if(checkString(s)){  //eg: given MIR125B1 and found MIR125B1
            return true;
        }
        else{
            s1=s.replace("-","");
            s1=s1.replace("(","");
            s1=s1.replace(")","");

            if(checkString(s1)){  //eg: given (MIR)-125b-1 and found MIR125B1
                return true;
            }
            else{
                sb=new StringBuilder();
                sb.append(s1);
                sb.append("1");

                if(checkString(sb.toString())){  //eg: given (MIR)-125b and found MIR125B1
                    return true;
                }



            }
        }

        for (int i = 0; i <children.size() ; i++) {
            if(children.get(i).findString(s)){
                return true;
            }
        }
        return false;
    }

    private boolean checkString(String s){
        return (name.equalsIgnoreCase(s));
    }


    public String toString(){
       return getString("");
    }

    /**
     * Goes up the tree searching tosee if the given node is an ancestor of this node.
     * @param n
     * @return
     */
    public boolean isAncestor(Node n){
        if(id.equalsIgnoreCase(n.id)){
            return true;
        }
        else if(parentNode==null){
            return false;
        }
        else{
            return parentNode.isAncestor(n);
        }
    }

    public String getString(String header){
        StringBuilder sb=new StringBuilder();
        sb.append(header);
        sb.append(name);
        sb.append("\n");
        String newheader="."+header;
        for (int i = 0; i <children.size() ; i++) {
            sb.append(children.get(i).getString(newheader));
        }
        return sb.toString();
    }

    public HashMap<String,Node> getGazetteerList(){
        HashMap<String,Node> gl=new HashMap<String,Node>();
        if(name.contains(",") && !name.contains(", and ")){
            String[] parts=name.split(",");
            if(parts.length==2){
                gl.put(parts[1]+" "+parts[0],this);
            }
            else{
                gl.put(name,this);
            }
        }
        else {
            gl.put(name,this);
        }
        for (int i = 0; i <children.size() ; i++) {
            gl.putAll(children.get(i).getGazetteerList());
        }
        return gl;
    }
}
