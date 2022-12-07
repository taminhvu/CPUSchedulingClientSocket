
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Ddnode implements Serializable{
    private String sl_node;
    private String chiphi="0";
    private ArrayList<String> nodes = new ArrayList<>();

    public Ddnode() {
    }

    public Ddnode(String sl_node) {
        this.sl_node = sl_node;
    }

    public String getChiphi() {
        return chiphi;
    }

    public void setChiphi(String chiphi) {
        this.chiphi = chiphi;
    }

    public String getSl_node() {
        return sl_node;
    }

    public void setSl_node(String sl_node) {
        this.sl_node = sl_node;
    }

    public ArrayList<String> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<String> nodes) {
        this.nodes = nodes;
    }
    public void insertNode(String node) throws Exception{
        this.nodes.add(node);
    }

    @Override
    public String toString() {
        String s=this.getSl_node();
        for (int i=0; i<this.nodes.size(); i++){
            s+="\n"+nodes.get(i);
        }
        return s;
    }


}
