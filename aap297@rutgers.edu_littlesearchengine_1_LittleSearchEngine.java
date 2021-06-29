package lse;

import java.io.*;
import java.security.AllPermission;
import java.util.*;

/**
 * author @aap297
 * 
 */

public class LittleSearchEngine {

    /**
     * This is a hash table of all keywords. The key is the actual keyword, and the
     * associated value is an array list of all occurrences of the keyword in
     * documents. The array list is maintained in DESCENDING order of frequencies.
     */
    HashMap<String, ArrayList<Occurrence>> keywordsIndex;

    /**
     * The hash set of all noise words.
     */
    HashSet<String> noiseWords;

    /**
     * Creates the keyWordsIndex and noiseWords hash tables.
     */
    public LittleSearchEngine() {
        keywordsIndex = new HashMap<String, ArrayList<Occurrence>>(1000, 2.0f);
        noiseWords = new HashSet<String>(100, 2.0f);
    }

    // this method works******************
    public HashMap<String, Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException {
        /** COMPLETE THIS METHOD **/
        if (docFile == null) {
            throw new FileNotFoundException("document file is not found on disk");
        }

        HashMap<String, Occurrence> temp = new HashMap<String, Occurrence>(1000, 2.0f);

        Scanner sc = new Scanner(new File(docFile));

        while (sc.hasNext()) {
            String worde = sc.next();
            worde = getKeyword(worde);
            if (worde != null) {

                if (temp.containsKey(worde)) {

                    Occurrence tempOccur = temp.get(worde);
                    tempOccur.frequency++;
                    temp.put(worde, tempOccur);
                } else {
                    Occurrence tempOccur = new Occurrence(docFile, 1);
                    temp.put(worde, tempOccur);
                }

            }

        }

        // following line is a placeholder to make the program compile
        // you should modify it as needed when you write your code
        return temp;
    }

    // this method works******************
    public void mergeKeywords(HashMap<String, Occurrence> kws) {
        /** COMPLETE THIS METHOD **/

        for (String s : kws.keySet()) {

            // if not in kewords create new Arratlist and add sort i
            if (!keywordsIndex.containsKey(s)) {
                ArrayList<Occurrence> insertNew = new ArrayList<Occurrence>();
                Occurrence recentOccurr = kws.get(s);
                insertNew.add(recentOccurr);
                // sorts
                // insertLastOccurrence(insertNew);
                keywordsIndex.put(s, insertNew);
            } else {

                ArrayList<Occurrence> merge = keywordsIndex.get(s);
                Occurrence temp = kws.get(s);
                merge.add(temp);
                insertLastOccurrence(merge);
                keywordsIndex.put(s, merge);

            }

        }

    }

    // this method works******************
    public String getKeyword(String word) {
        boolean trigger = false;

        if (word == null) {
            return null;
        }
        String tword = word.toLowerCase();
        char[] charArray = tword.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (trigger && ch >= 'a' && ch <= 'z') {
                return null;
            }
            if (!(ch >= 'a' && ch <= 'z') && i < charArray.length - 1) {
                trigger = true;
                // return null;
            }

        }
        int k = 0;
        // Put this firt or not IndexOutBounds
        while (k < charArray.length && charArray[k] >= 'a' && charArray[k] <= 'z') {
            k++;
        }
        tword = tword.substring(0, k);

        if (noiseWords.contains(tword)) {
            return null;
        } else {
            return tword;
        }

    }

    // This method Works***************
    public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
        /** COMPLETE THIS METHOD **/

        ArrayList<Integer> midPoints = new ArrayList<Integer>();
        if (occs.size() == 1) {
            return null;
        }

        // gets the Occurnece object that needs to be sorted
        Occurrence insertedOccurr = occs.get(occs.size() - 1);

        // removes inserted Occurence
        occs.remove(occs.size() - 1);

        int low = 0, high = occs.size() - 1;
        int mid = 0;

        while (high >= low) {
            mid = low + (high - low) / 2;
            int freq = occs.get(mid).frequency;

            if (freq == insertedOccurr.frequency) {
                midPoints.add(mid);
                break;
            }
            if (freq < insertedOccurr.frequency) {
                high = mid - 1;
                midPoints.add(mid);
            }
            if (freq > insertedOccurr.frequency) {
                low = mid + 1;
                midPoints.add(mid);
            }
        }
        if (occs.get(mid).frequency > insertedOccurr.frequency) {
            occs.add(mid + 1, insertedOccurr);
        } else {
            occs.add(mid, insertedOccurr);
        }

        // if we reach here, then element was
        // not present

        // following line is a placeholder to make the program compile

        // you should modify it as needed when you write your code
        return midPoints;
    }

    public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
        // load noise words to hash table
        Scanner sc = new Scanner(new File(noiseWordsFile));
        while (sc.hasNext()) {
            String word = sc.next();
            noiseWords.add(word);

        }

        // index all keywords
        sc = new Scanner(new File(docsFile));
        while (sc.hasNext()) {
            String docFile = sc.next();
            HashMap<String, Occurrence> kws = loadKeywordsFromDocument(docFile);
            mergeKeywords(kws);

        }
        // System.out.println(keywordsIndex);
        sc.close();
    }

    public ArrayList<String> top5search(String kw1, String kw2) {
        /** COMPLETE THIS METHOD **/
        ArrayList<String> top5 = new ArrayList<String>();

        ArrayList<Occurrence> kw1Array = keywordsIndex.get(kw1);
        int kw1Index = 0;
        ArrayList<Occurrence> kw2Array = keywordsIndex.get(kw2);
        int kw2Index = 0;

        if (keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {

            while (kw1Index < kw1Array.size() && kw2Index < kw2Array.size() && top5.size() < 5) {

                if (kw1Array.get(kw1Index).frequency > kw2Array.get(kw2Index).frequency) {
                    if (!top5.contains(kw1Array.get(kw1Index).document)) {
                        top5.add(kw1Array.get(kw1Index).document);
                        kw1Index++;
                    } else {
                        kw1Index++;
                    }

                } else if (kw1Array.get(kw1Index).frequency < kw2Array.get(kw2Index).frequency) {
                    if (!top5.contains(kw2Array.get(kw2Index).document)) {
                        top5.add(kw2Array.get(kw2Index).document);
                        kw2Index++;
                    } else {
                        kw2Index++;
                    }

                } else {
                    if (!top5.contains(kw1Array.get(kw1Index).document)) {
                        top5.add(kw1Array.get(kw1Index).document);
                        kw1Index++;
                    } else if (!top5.contains(kw2Array.get(kw2Index).document)) {
                        top5.add(kw2Array.get(kw2Index).document);
                        kw2Index++;
                    } else {
                        kw1Index++;
                        kw2Index++;
                    }

                }
            }
            if (top5.size() < 5) {
                if (kw1Index < kw1Array.size()) {
                    while (top5.size() < 5 && kw1Index < kw1Array.size()) {
                        if (!top5.contains(kw1Array.get(kw1Index).document)) {
                            top5.add(kw1Array.get(kw1Index).document);
                        }

                        kw1Index++;
                    }
                }
            }
            if (top5.size() < 5) {
                if (kw2Index < kw2Array.size()) {
                    while (top5.size() < 5 && kw2Index < kw2Array.size()) {
                        if (!top5.contains(kw2Array.get(kw2Index).document)) {
                            top5.add(kw2Array.get(kw2Index).document);
                        }
                        kw2Index++;
                    }
                }
            }

        }
        // insert up to the 4th index. Breaks if there is less then 5 in list of kw1
        else if (keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
            while (kw1Index < kw1Array.size() && top5.size() < 5) {
                top5.add(kw1Array.get(kw1Index).document);
                kw1Index++;

            }
        }
        // insert up to the 4th index. Breaks if there is less then 5 in list of kw2
        else if (!keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
            while (kw2Index < kw2Array.size() && top5.size() < 5) {
                top5.add(kw2Array.get(kw2Index).document);
                kw2Index++;

            }
        } else {
            return null;
        }

        // following line is a placeholder to make the program compile
        // you should modify it as needed when you write your code
        return top5;

    }
}