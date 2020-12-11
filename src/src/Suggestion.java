package src;
import java.util.*;
public class Suggestion
{ //TrieNode class
     class TrieNode
    { TrieNode[] nextNode=new TrieNode[128]; //link to the next TrieNode
        boolean finished; //value is true when it is the last alphabet(or node) of the word
        TrieNode()
        { finished=false;
            for (int i=0;i<128;i++)
                nextNode[i]=null;
        }
    }
     TrieNode root;
     String keywords = "abstract continue for new switch default goto package synchronized boolean do if private this break double implements protected throw byte else import public throws case enum instanceof return transient catch extends short try char final interface void class finally long volatile const float native super while int";
    //To insert a word in the trie structure

    Suggestion(){
        this.root = new Suggestion.TrieNode();
        this.addBasics();
    }
     void insert(String key) {
        int length=key.length();
        TrieNode current=root;
        for(int i=0;i<length;i++){
            int k=key.charAt(i);
            if(current.nextNode[k]==null)
                current.nextNode[k]=new TrieNode();
            current=current.nextNode[k];
        }
        current.finished=true;
    }
    //return the list of words whose prefix is the given key
    //called by autoComplete function - a recursive function

     List<String> getAllNext(TrieNode node){
        List<String> ans=new ArrayList<String>();
        for(int i=0;i<128;i++){
            if(node.nextNode[i]!=null) {
                char x='\0';
                x+=i;
                String temp=""+x;
                List<String> arr=getAllNext(node.nextNode[i]);
                for(int k=0;k<arr.size();k++)
                ans.add(temp+arr.get(k));
                if(arr.size()==0)
                    ans.add(temp);
            }
        }
        return ans;
    }
    //return the list of words whose prefix is the given key
    //this function checks is the key is a valid prefix and traverses till the node that is the end of the prefix

     List<String> autoComplete(String key) {
        List<String> allString=new ArrayList<String>();
        int length=key.length();
        TrieNode current=root;
        for(int i=0;i<length;i++) {
            int k=key.charAt(i);
            if(current.nextNode[k]==null)
             return allString;
            current=current.nextNode[k];
        }
        allString=getAllNext(current);
        return allString;
    }
    //inserts primitive datatypes in the trie

     void addBasics() {
      //  this.insert("int");
        for(String s:keywords.split(" ")){
            if(s.equals("int"))System.out.println(s);
            if(!s.equals(""))this.insert(s.trim());
        }
    }
}

