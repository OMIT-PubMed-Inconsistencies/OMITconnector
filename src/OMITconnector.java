import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OMITconnector {

    private static HashMap<String,Node> nodeMap=new HashMap<String,Node>();
    private static OMITconnector oc=new OMITconnector();


    public static void main(String[] args) {
        Node MeShroot=oc.getTreeRootedAt("&obo;OMIT_0000110");
        oc.print(MeShroot);
       // Node n=MeShroot.findNode("macrophages");
        //oc.print(n);
    }

    private OMITconnector() {
        this.ReadOntologyFile();
    }

    public Node getTreeRootedAt(String id){
        return(nodeMap.get(id));
    }

    public void print(ArrayList<Node> n){
        for (int i = 0; i <n.size() ; i++) {
            print(n.get(i));
        }
    }

    public void print(Node n){
        System.out.println(n.toString());
    }

    public static OMITconnector getInstance(){
        return oc;
    }

    private void ReadOntologyFile() {
        Node n=null;
        BufferedReader br =null;


        try {

            try {
                br = new BufferedReader(new FileReader("../OMIT.owl"));
            }
            catch(FileNotFoundException e){
                br = new BufferedReader(new FileReader("OMIT.owl"));
            }
            //  StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {

                if(line.contains("<owl:Class rdf:about=\"&obo;OMIT")||line.contains("<owl:Class rdf:about=\"&obo;NCRO")) { //This is the header      //
                    String id = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                    // System.out.println(id);

                    line = br.readLine(); //read name
                    try {
                        String name = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                        //System.out.println(name);

                        line = br.readLine(); //read parent
                        String parent = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                        //System.out.println(parent);

                        n = new Node(id, name, parent);
                        nodeMap.put(id, n);
                    } catch (Exception e) {
                        //ran out of lines
                    }
                }
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> iter=nodeMap.keySet().iterator();

        while(iter.hasNext()){
            String id=iter.next();
            n=nodeMap.get(id);
            String parentId=n.parent;
            //System.out.println(parentId);
            Node pNode = nodeMap.get(parentId);
            if(pNode!=null) {
                pNode.addChild(n);
            }

        }

    }
}
