import java.util.*;

class SetImplementation {
    private static class Node {
        int x;
        int y;
        int gScore; // cost from start to current node
        int hScore; // heuristic score (estimated cost from current node to goal)
        int fScore; // total score (gScore + hScore)
        Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            gScore = Integer.MAX_VALUE;
            hScore = 0;
            fScore = Integer.MAX_VALUE;
            parent = null;
        }
    }

    public static List<Node> findPath(int[][] grid, int startX, int startY, int endX, int endY) {
        int numRows = grid.length;
        int numCols = grid[0].length;

        if (startX < 0 || startX >= numRows || startY < 0 || startY >= numCols ||
            endX < 0 || endX >= numRows || endY < 0 || endY >= numCols) {
            throw new IllegalArgumentException("Invalid start or end position");
        }

        // Define movement directions (cardinal directions and diagonal directions)
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        // Create the open set and the closed set
        Set<Node> openSet = new HashSet<>();
        Set<Node> closedSet = new HashSet<>();

        // Create the start and end nodes
        Node startNode = new Node(startX, startY);
        Node endNode = new Node(endX, endY);

        // Set the gScore and fScore of the start node
        startNode.gScore = 0;
        startNode.fScore = calculateHeuristic(startNode, endNode);

        // Add the start node to the open set
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            // Get the node with the lowest fScore from the open set
            Node currentNode = openSet.stream().min(Comparator.comparingInt(node -> node.fScore)).orElse(null);

            // If the current node is the goal node, reconstruct and return the path
            if (currentNode == endNode) {
                return reconstructPath(currentNode);
            }

            // Move the current node from the open set to the closed set
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            // Explore the neighbors of the current node
            for (int[] direction : directions) {
                int newX = currentNode.x + direction[0];
                int newY = currentNode.y + direction[1];

                // Check if the new position is within the grid bounds
                if (newX < 0 || newX >= numRows || newY < 0 || newY >= numCols) {
                    continue;
                }

                // Create a new neighbor node
                Node neighbor = new Node(newX, newY);

                // Skip if the neighbor is an obstacle or already in the closed set
                if (grid[newX][newY] == 1 || closedSet.contains(neighbor)) {
                    continue;
                }

                // Calculate the tentative gScore for the neighbor
                int tentativeGScore = currentNode.gScore + 1;

                // Check if the neighbor is already in the open set
                boolean isNewNode = !openSet.contains(neighbor);

                if (isNewNode || tentativeGScore < neighbor.gScore) {
                    // Update the neighbor's gScore
