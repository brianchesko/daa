/*
 * Purpose: Design and Analysis of Algorithms Assignment 4
 * Status: Complete and thoroughly tested
 * Last update: 04/18/18
 * Submitted:  04/18/18
 * Comment: test suite and sample run attached
 * @author: Brian Chesko and Joseph Leclercq
 * @version: 2018.04.18
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Driver {
    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String args[]) throws IOException {
        SpecializedMinHeap heap = new SpecializedMinHeap();
        int command;
        int key;
        int value;
        System.out.println();
        System.out.println("Select from the following menu:\n" +
                           "    0.	Exit the program.\n" +
                           "    1.	Check if MinHeap is empty.\n" +
                           "    2.	Insert key, value in MinHeap.\n" +
                           "    3.	Delete min value from MinHeap.\n" +
                           "    4.	Decrease key's value in MinHeap.\n" +
                           "    5.	Display items in MinHeap in array" +
                           " order.\n");
        System.out.println();

        do {
            command = IOTools.promptInteger("Make your menu selection "
                + "now: ", 0, 6);
            switch (command) {
                case 0:
                    System.out.println("Goodbye!");
                    System.exit(1);
                    break;
                case 1:
                    if(heap.isEmpty()){
                        System.out.println("The heap is empty");    
                    }
                    else{
                        System.out.println("The heap is not empty");    
                    }
                    break;
                case 2:
                    key = IOTools.promptInteger("What is the" + 
                                " key you are are adding?",0,
                                Integer.MAX_VALUE);
                    value = IOTools.promptPositiveInteger("What is " +
                                "the value you are adding?");
                    heap.insert(key, value);
                    break;
                case 3:
                    heap.deleteMin();
                    break;
                case 4:
                    key = IOTools.promptInteger("What is the" + 
                                " key you are are decreasing?",0,
                                Integer.MAX_VALUE);
                    value = IOTools.promptPositiveInteger("What is " +
                                "the new value you are lowering to?");
                    if(heap.decreaseKey(key, value)){
                        System.out.println("Value lowered");
                    }
                    else{
                        System.out.println("Not a valid key");
                    }
                    break;
                case 5:
                    heap.printInfo();
                    break;
                default:
                    System.out.println("Not an acceptable menu selection");
            }
            System.out.println();
        } while(true);
    }
}
