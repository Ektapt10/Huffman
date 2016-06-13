package com.Tree.Huffman;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
 
abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final Integer numberOfOccurrences; 
    public HuffmanTree(int occurrences) { this.numberOfOccurrences = occurrences; }
 
    // frequency comparission
    @Override
    public int compareTo(HuffmanTree tree) {
        return this.numberOfOccurrences - tree.numberOfOccurrences;
    }
}
 
class HuffmanLeaf extends HuffmanTree {
	// the character this child represents
	public char valueOfLeaf; 
    public Integer key;
 
    public HuffmanLeaf(int frequency, char value) {
        super(frequency);
        this.valueOfLeaf = value;
    }

	public HuffmanLeaf(Integer key, Integer freqOfKeyinInt) {
		 super(freqOfKeyinInt);
		 this.key = key;
	}

}
 
class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; 
 
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.numberOfOccurrences + r.numberOfOccurrences);
        this.left = l;
        this.right = r;
    }
}
 
public class Papa_Huffman {

	public static HuffmanTree createHuffmanTree(ArrayList<HuffmanLeaf> orderedCharValues) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        
               
        for (int i = 0; i < orderedCharValues.size(); i++)
            if (orderedCharValues.get(i).numberOfOccurrences > 0)
                trees.offer(new HuffmanLeaf(orderedCharValues.get(i).numberOfOccurrences, (char)(int)orderedCharValues.get(i).key));
 
       
        while (trees.size() > 1) {
        	
             
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
            
            
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
    public static String generateHeader(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;

            prefix.append('1');
            prefix.append(leaf.valueOfLeaf);
 
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            // over left
            generateHeader(node.left, prefix);
 
            // over right
            generateHeader(node.right, prefix);
            prefix.append('0');
        }
		return prefix.toString();
    }
 
    public static void main(String[] args) {
        String test = "go go gophers";
 
        //converting byte array
        byte[] inputBuffer = new byte[(int)test.length()];
        int i =0;
        for (char c : test.toCharArray()){
        	inputBuffer[i]= (byte) c;
        	i++;
        }
        
        
        Map<Integer, Integer> occurrencesToValueMapping =  buildMap(inputBuffer);
        
        
        ArrayList<HuffmanLeaf> orderedNodes = buildOrderedNodes(occurrencesToValueMapping);
        
        
        for (int k=0;k<orderedNodes.size();k++) {
			System.out.println("alphabet---->"+(char)(int)orderedNodes.get(k).key+ "--->" +"frequency of aplhabet---->"+orderedNodes.get(k).numberOfOccurrences);
		}

        
        HuffmanTree tree = createHuffmanTree(orderedNodes);
        String totalNumberOfChars = tree.numberOfOccurrences.toString();

        
        
        String header = generateHeader(tree, new StringBuffer());
        System.out.println("Header--->"+header+"0"+totalNumberOfChars+"#");
    }
    
    /**
     * Building tree map of input byte value and its frequency of occurrence
     * @param value
     * @return
     */
    private static Map<Integer, Integer> buildMap(byte[] value) {
        Map<Integer, Integer> occurrencesToValueMapping = new TreeMap<Integer, Integer>();
        
        for (byte inputValue : value) {
            int inputByteValue = inputValue & 0x007F;
            
            if (occurrencesToValueMapping.containsKey(inputByteValue)) {
                occurrencesToValueMapping.put(inputByteValue, occurrencesToValueMapping.get(inputByteValue) + 1);
            }
            else {
                occurrencesToValueMapping.put(inputByteValue, 1);
            }
        }
       
        return occurrencesToValueMapping;
    }
    
    /**
     * Ordering the tree map values according to their frequency of occurrence and byte value
     * @param occurrencesToValueMapping
     * @return
     */
    private static ArrayList<HuffmanLeaf> buildOrderedNodes(Map<Integer, Integer> occurrencesToValueMapping) {
        ArrayList<HuffmanLeaf> orderedNodes = new ArrayList<HuffmanLeaf>();
        
        for (Integer key : occurrencesToValueMapping.keySet()) {
        	orderedNodes.add(new HuffmanLeaf(key, occurrencesToValueMapping.get(key)));
        }
        
        Collections.sort(orderedNodes);
        return orderedNodes;
    }
}
