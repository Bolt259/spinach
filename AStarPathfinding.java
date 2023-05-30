import java.util.*;

public class AStarPathfinding {
    
    private static class Node {
        int x;
        int y;
        int gScore;
        int hScore;
        int fScore;
        Node parent;

        public Node(int x, int y, int gScore, int hScore, Node parent) {
            this.x = x;
            this.y = y;
            this.gScore = gScore;
            this.hScore = hScore;
            this.fScore = gScore + hScore;
            this.parent = parent;
        }
    }

    public static List<Node> findPath(int[][] grid, Node start, Node goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.fScore));
        Set<Node> closedSet = new HashSet<>();

        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current == goal) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            List<Node> neighbors = getNeighbors(current, grid);
            for (Node neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeGScore = current.gScore + 1;

                if (!openSet.contains(neighbor) || tentativeGScore < neighbor.gScore) {
                    neighbor.gScore = tentativeGScore;
                    neighbor.hScore = calculateHeuristic(neighbor, goal);
                    neighbor.fScore = neighbor.gScore + neighbor.hScore;
                    neighbor.parent = current;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null; // No path found
    }

    private static List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node);
            node = node.parent;
        }
        return path;
    }

    private static List<Node> getNeighbors(Node node, int[][] grid) {
        List<Node> neighbors = new ArrayList<>();
        int x = node.x;
        int y = node.y;

        if (x > 0 && grid[y][x - 1] == 0) {
            neighbors.add(new Node(x - 1, y, 0, 0, null));
        }
        if (x < grid[0].length - 1 && grid[y][x + 1] == 0) {
            neighbors.add(new Node(x + 1, y, 0, 0, null));
        }
        if (y > 0 && grid[y - 1][x] == 0) {
            neighbors.add(new Node(x, y - 1, 0, 0, null));
        }
        if (y < grid.length - 1 && grid[y + 1][x] == 0) {
            neighbors.add(new Node(x, y + 1, 0, 0, null));
        }

        return neighbors;
    }

    private static int calculateHeuristic(Node node, Node goal) {
        return Math.abs(node.x - goal.x) + Math.abs(node.y - goal.y);
    }

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
        };

        Node start = new Node(0, 0, 0, 0, null);
        Node goal = new Node(3, 3, 0, 0, null);

        List<Node> path = findPath(grid, start, goal);
        if (path != null) {
            for (Node node : path) {
                System.out.println("(" + node.x + ", " + node.y + ")");
            }
        } else {
            System.out.println("No path found.");
        }
    }
}
