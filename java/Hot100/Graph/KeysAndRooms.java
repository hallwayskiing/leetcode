package Hot100.Graph;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * There are n rooms labeled from 0 to n - 1 and all the rooms are locked except for room 0.
 * Your goal is to visit all the rooms.
 * However, you cannot enter a locked room without having its key.
 * <p>
 * When you visit a room, you may find a set of distinct keys in it.
 * Each key has a number on it, denoting which room it unlocks,
 * and you can take all of them with you to unlock the other rooms.
 * <p>
 * Given an array rooms where rooms[i] is the set of keys that you can obtain if you visited room i,
 * return true if you can visit all the rooms, or false otherwise.
 */
public class KeysAndRooms {
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] unlocked = new boolean[n];
        unlocked[0] = true;

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);

        while (!queue.isEmpty()){
            int room = queue.poll();
            List<Integer> keys = rooms.get(room);
            for (int key:keys){
                if(!unlocked[key]){
                    queue.offer(key);
                    unlocked[key] = true;
                }
            }
        }

        for (boolean room:unlocked){
            if (!room)
                return false;
        }

        return true;
    }

    /**
     * DFS
     */
    public boolean canVisitAllRoomsII(List<List<Integer>> rooms) {
        boolean[] visited = new boolean[rooms.size()];
        dfs(rooms, 0, visited);

        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    private void dfs(List<List<Integer>> rooms, int room, boolean[] visited) {
        visited[room] = true;
        for (int key : rooms.get(room)) {
            if (!visited[key]) {
                dfs(rooms, key, visited);
            }
        }
    }
}
