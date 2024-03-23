package myapp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoComplete {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        List<String> meanings = new ArrayList<>();
    }

    static class Trie {
        TrieNode root = new TrieNode();

        void insert(String word, String meaning ) {
            TrieNode node = root;
            for (char ch : word.toCharArray()) {
                node.children.putIfAbsent(ch, new TrieNode());
                node = node.children.get(ch);
            }
            node.isEnd = true;
            node.meanings.add(meaning);

        }
        void remove(String word) {
            remove(root, word, 0);
        }

        private boolean remove(TrieNode node, String word, int depth) {
            if (node == null) {
                return false;
            }

            if (depth == word.length()) {
                if (!node.isEnd) {
                    return false;
                }
                node.isEnd = false;
                return node.children.isEmpty();
            }

            char ch = word.charAt(depth);
            TrieNode nextNode = node.children.get(ch);
            if (nextNode == null) {
                System.out.println("Cannot find: " + word);
                return false;
            }

            boolean shouldDeleteCurrentNode = remove(nextNode, word, depth + 1);

            if (shouldDeleteCurrentNode) {
                node.children.remove(ch);
                return node.children.isEmpty();
            }

            return false;
        }
        public boolean contains(String word) {
            TrieNode node = root;

            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);

                if (!node.children.containsKey(ch)) {
                    return false;
                }

                node = node.children.get(ch);
            }

            return node.isEnd;
        }

        List<String> autocompleteWithMeanings(String prefix) {
            TrieNode node = root;
            List<String> res = new ArrayList<>();
            for (char ch : prefix.toCharArray()) {
                node = node.children.get(ch);
                if (node == null) {
                    return new ArrayList<>();
                }
            }
            helper(node, res, prefix);
            return res;
        }

        void helper(TrieNode node, List<String> res, String prefix) {
            if (node == null)
                return;

            if (node.isEnd) {
                for (String meaning : node.meanings) {
                    res.add(prefix + " - " + meaning);
                }
            }
            for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                helper(entry.getValue(), res, prefix + entry.getKey());
            }
        }
    }

    public static Trie buildTrieFromFile(String fileName) {
        Trie trie = new Trie();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t");
                if (parts.length > 1) {
                    //System.out.println(parts[0]+"/"+parts[1]);
                    trie.insert(parts[0], parts[1]);

                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return trie;
    }
}
