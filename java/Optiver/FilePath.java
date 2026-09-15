package Optiver;

import java.util.HashMap;
import java.util.Map;

public class FilePath {
    public static class Trie {
        Map<String, Trie> children;
        boolean isEnd;
        String value;

        public Trie() {
            children = new HashMap<>();
            isEnd = false;
            value = null;
        }
    }

    private final Trie node = new Trie();

    public boolean create(String path, String value) {
        String[] parts = normalize(path);
        if (parts == null) {
            return false;
        }

        Trie curr = node;
        for (String part : parts) {
            curr.children.putIfAbsent(part, new Trie());
            curr = curr.children.get(part);
        }

        if (curr.isEnd) {
            return false;
        }

        curr.isEnd = true;
        curr.value = value;
        return true;
    }

    public String retrieve(String path) {
        String[] parts = normalize(path);
        if (parts == null) {
            return null;
        }

        Trie curr = node;
        for (String part : parts) {
            curr = curr.children.get(part);
            if (curr == null) {
                return null;
            }
        }

        return curr.isEnd ? curr.value : null;
    }

    private String[] normalize(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        return java.util.Arrays.stream(path.split("/"))
                .filter(part -> !part.isEmpty())
                .toArray(String[]::new);
    }
}

