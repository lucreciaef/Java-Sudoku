package PseudokuAssignmentJava;

import java.util.Arrays;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class Pseudoku {

    public static class Pair {
        public boolean foundInStack;
        public Stack<Integer> remainingStack;

        public Pair(boolean foundInStack, Stack<Integer> remainingStack) {
            this.foundInStack = foundInStack;
            this.remainingStack = remainingStack;
        }
    }

    public static void main(String[] args) {

        int[] row = {1,2,3,4};

        MakeSolution(row);

    }

    //---------------------------------------------------------------------
    //TASK 1: take a four-element vector called row as an input parameter and return a vector of 4 elements called puzzle
    public static int[][] MakeVector(int[] row) {
        int[][] puzzle = new int[4][4];
        for (int i = 0; i < row.length; i++) {
            puzzle[i] = row;
        }
        return puzzle;
    }

    //---------------------------------------------------------------------
    //TASK 2: Cyclic permutation using queue logic, until the numbers in all rows satisfy conditions
    //shift all values of the elements one place to the left (or the right)
    //with the value at the end going to the other end.
    public static int[] PermuteVector(int[] row, int p) {
        Queue<Integer> rowConvertedToQueue = new ArrayBlockingQueue<>(4);
        for (int rowEntry:row) {
            rowConvertedToQueue.add(rowEntry);
        }
        if (p == 0) {
            return rowConvertedToQueue.stream().mapToInt(Integer::intValue).toArray();
        }
        else {
            for (int i = 0; i < p; i++) {
                rowConvertedToQueue.add(rowConvertedToQueue.poll());
            }
        }
        return rowConvertedToQueue.stream().mapToInt(Integer::intValue).toArray();
    }

    //TASK 3: Function takes a 4-element vector puzzle, which is output of MakeVector as an input parameter, and x y z integers
    // The function will return puzzle but with elements puzzle[1], puzzle[2] and puzzle[3] cyclically permuted by x, y and z elements
    // respectively to the left: x, y and z should all be numbers between 0 and 3 (inclusive).
    // To be able to get full marks you should call the function PermuteVector appropriately. HINT: You do not need to loop over integers x, y and z.
    public static int[][] PermuteRows(int[][] puzzle, int x, int y, int z) {
        int[][] permutedPuzzle = new int[4][4];

        for (int i = 0; i < puzzle.length; i++) {
            int tempP = 0;

            switch (i){
                case 1:
                    tempP = x;
                    break;
                case 2:
                    tempP = y;
                    break;
                case 3:
                    tempP = z;
                    break;
            }
            permutedPuzzle[i] = PermuteVector(puzzle[i], tempP);
        }
        return permutedPuzzle;
    }

    //TASK 4: This function will take a stack and a value (item) as input parameters. Return FALSE if item is not stored in the stack
    //If it is: Return the stack without the element storing item
    public static Pair SearchStack(Stack<Integer> stack, int item) {
        Stack<Integer> tempStack = new Stack<>();
        boolean isItemFoundInStack = false;
        int itemCount = stack.size();

        for (int i = 0; i < itemCount; i++) {
            if (stack.peek() != item) {
                tempStack.push(stack.pop());
            }
            else {
                stack.pop();
                isItemFoundInStack = true;
            }
        }
        itemCount = tempStack.size();
        Stack<Integer> searchedStack = new Stack<>();

        for (int i = 0; i < itemCount; i++) {
            searchedStack.push(tempStack.pop());
        }
        return new Pair(isItemFoundInStack, searchedStack);
    }
    
    //TASK 5: Take the vector puzzle (from MakeVector) as param. and check that column j contains all numbers from 1 to 4. Return T or F
    public static boolean CheckColumn(int[][] puzzle, int j) {
        Stack<Integer> numbers = new Stack<>();
        for (int m = 1; m <= 4; m++) {
            numbers.push(m);
        }

        for (int k = 0; k < 4; k++) {
            int value = puzzle[k][j];
            Pair resultOfSearchedStack = SearchStack(numbers, value);

            if (resultOfSearchedStack.foundInStack) {
                numbers = resultOfSearchedStack.remainingStack;
            } else {
                //System.out.println("Col check failed");
                return false;
            }
        }
        return true;
    }

    public static boolean ColChecks(int[][] puzzle) {
        for (int j = 0; j < puzzle[0].length; j++) {
            if (!CheckColumn(puzzle,j)) {
                return false;
            }
        }
        return true;
    }

    //TASK 6: Take puzzle from MakeVector as input and check the sub-grids using SearchStack
    public static boolean CheckGrids(int[][] puzzle) {
        for (int cellX = 0; cellX < 2; cellX++) {
            for (int cellY = 0; cellY < 2; cellY++) {

                Stack<Integer> numbers = new Stack<>();
                for (int m = 1; m <= 4; m++) {
                    numbers.push(m);
                }

                for (int subCellX = 0; subCellX < 2; subCellX++) {
                    for (int subCellY = 0; subCellY < 2; subCellY++) {
                        int value = puzzle[cellX * 2 + subCellX][cellY * 2 + subCellY];

                        Pair resultOfSearchedStack = SearchStack(numbers, value);

                        if (resultOfSearchedStack.foundInStack) {
                            numbers = resultOfSearchedStack.remainingStack;
                        } else {
                            //System.out.println("Grid check failed");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    //******************************************
    //TASK 7: Implementing puzzle vectors -> Explanation on document
    // Draw data structure, explain how operations are implemented
    //******************************************

    //TASK 8: This function will take the four-element vector row as input, which is the same input for
    // the function MakeVector. The function should return a solved Pseudoku puzzle such that all column
    // and sub-grid Pseudoku conditions are satisfied. The function will generate a vector using MakeVector(row),
    // then try cyclic permutations on this vector using PermuteRow(puzzle, x, y, z) until a set of permutations
    // is found such that all Pseudoku conditions are satisfied (checked using CheckGrids and ColCheck).

    public static int[][] MakeSolution(int[] row) {
        int[][] generatedVector = MakeVector(row);
        int[][] solvedPuzzle = solveByPermutation(generatedVector);

        System.out.println("puzzle solution:");
        for (int[] ints : solvedPuzzle) {
            System.out.println(Arrays.toString(ints));
        }
        return solvedPuzzle;
    }

    public static int[][] solveByPermutation(int[][] generatedVector){
        int[][] permutedPuzzle = null;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    permutedPuzzle = PermuteRows(generatedVector, x, y, z);
                    if (ColChecks(permutedPuzzle) && CheckGrids(permutedPuzzle)) {
                        return permutedPuzzle;
                    }
                }
            }
        }
        return permutedPuzzle;
    }
}
