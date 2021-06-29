package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author aap297
 *
 */
public class Trie {

    // prevent instantiation
    private Trie() {
    }

    /**
     * Builds a trie by inserting all words in the input array, one at a time, in
     * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
     * input array are all lower case.
     * 
     * @param allWords Input array of words (lowercase) to be inserted.
     * @return Root of trie with all words inserted from the input array
     */
    public static TrieNode buildTrie(String[] allWords) {
        TrieNode root = new TrieNode(null, null, null);
        if (allWords.length == 0)
            return root;

        TrieNode first = new TrieNode(new Indexes(0, (short) 0, (short) (allWords[0].length() - 1)), null, null);
        root.firstChild = first;

        TrieNode p = root.firstChild;
        TrieNode last = null;

        for (int i = 1; i < allWords.length; i++) {
            String pre = "";
            String add = allWords[i];

            while (p != null) {
                int wi = p.substr.wordIndex;
                int si = p.substr.startIndex;
                int ei = p.substr.endIndex;
                String node = allWords[wi].substring(si, ei + 1);
                if (add.indexOf(pre + node) == 0) {
                    pre = pre + node;
                    last = p;
                    p = p.firstChild;
                    continue;
                }

                if (add.charAt(si) != node.charAt(0)) {
                    last = p;
                    p = p.sibling;
                    continue;
                }

                int x = 0;
                for (int j = 0; j < node.length(); j++) {
                    if (add.charAt(si + j) == node.charAt(j))
                        x++;
                    else
                        break;
                }
                int y = si + x;

                TrieNode temp = new TrieNode(new Indexes(wi, (short) y, (short) ei), p.firstChild, null);
                p.firstChild = temp;
                p.substr.endIndex = (short) (y - 1);

                pre = pre + allWords[wi].substring(si, y);

                last = p;
                p = p.firstChild;
            }

            Indexes lastIndex = new Indexes(i, (short) (pre.length()), (short) (add.length() - 1));
            TrieNode z = new TrieNode(lastIndex, null, null);

            last.sibling = z;
            p = root.firstChild;
            last = null;
        }

        return root;
    }

    public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {

        ArrayList<TrieNode> completionList = new ArrayList<>();
        TrieNode p = root.firstChild, last = null;
        String total = "";

        while (prefix.length() > total.length() && p != null) {
            int wi = p.substr.wordIndex;
            int si = p.substr.startIndex;
            int ei = p.substr.endIndex;
            last = p;
            String c = allWords[wi].substring(si, ei + 1);
            if (prefix.indexOf(total + c) == 0) {
                total = total + c;
                p = p.firstChild;
                continue;
            }
            if ((total + c).indexOf(prefix) == 0) {
                p = p.firstChild;
                break;
            }

            p = p.sibling;
        }
        if (p == null) {
            if (allWords[last.substr.wordIndex].indexOf(prefix) == 0) {
                completionList.add(last);
                return completionList;
            }
            return null;
        }

        methodOne(completionList, p);
        return completionList;
    }

    private static void methodOne(ArrayList<TrieNode> completionList, TrieNode start) {
        if (start == null)
            return;
        if (start.firstChild == null) {
            completionList.add(start);
            methodOne(completionList, start.sibling);
            return;
        }
        methodOne(completionList, start.firstChild);
        methodOne(completionList, start.sibling);

    }

    public static void print(TrieNode root, String[] allWords) {
        System.out.println("\nTRIE\n");
        print(root, 1, allWords);
    }

    private static void print(TrieNode root, int indent, String[] words) {
        if (root == null) {
            return;
        }
        for (int i = 0; i < indent - 1; i++) {
            System.out.print("    ");
        }

        if (root.substr != null) {
            String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
            System.out.println("      " + pre);
        }

        for (int i = 0; i < indent - 1; i++) {
            System.out.print("    ");
        }
        System.out.print(" ---");
        if (root.substr == null) {
            System.out.println("root");
        } else {
            System.out.println(root.substr);
        }

        for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
            for (int i = 0; i < indent - 1; i++) {
                System.out.print("    ");
            }
            System.out.println("     |");
            print(ptr, indent + 1, words);
        }
    }
}