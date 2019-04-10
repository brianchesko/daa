/*
 * Purpose: Design and Analysis of Algorithms Assignment 1 Problem 1
 * Status: Complete and thoroughly tested
 * Last update: 02/02/19
 * Submitted:  02/04/19
 * Comment: testing run included in logfile "testing"
 * @author: Brian Chesko
 * @version: 2019.02.02
 */

#include <stdio.h>
#include <stdlib.h>

void queens(int);
int good_col(int, int, int*);
int good_diags(int, int, int*);
void print_forward(int*);

int num;
int comparisons = 0;

int main() {
    int n;
    printf("Enter a board size: ");
    scanf("%d", &n);
    printf("%d\n", n);

    num = n;
    queens(n);
}

void queens(int num) {
    int* arr = malloc(sizeof(int) * num);
    if (arr == NULL) {
        printf("Somehow don't have enough memory. Huh?");
        exit(1);
    }

    int i, j, k;
    // 0: not done, 1: done but failed, 2: successful solution
    int done = 0;

    // Initialize the arrays. No columns are in use but we'll need to
    // check columns even if that same row num isn't used, so must
    // set these to negative by default.
    for (i = 0; i < num; i++) {
        arr[i] = -1;
    }

    int init_col = 0;
    // Loop over each row.
    for (i = 0; i < num && !done; i++) {
        int placed = 0;
        // Iterate over columns to place queen, starting at 0.
        // 'placed' variable acts as a boolean flag, continue to next row on 1
        for (j = init_col; j < num && placed == 0; j++) {
            // Check previous rows/columns/diagonals. 
            if (arr[j] < 0 && good_diags(i, j, arr)) {
                comparisons++;
                if (arr[i] >= 0) {
                    arr[i] = j;
                } else {
                    arr[i] = -j - 1;
                }
                
                arr[j] = -arr[j] - 1;
                // Last row, successful board.
                // Set the toggle to done so we can stop backtracking. 
                if (i == num - 1) {
                    print_forward(arr);
                    done = 2;
                } else { // Successful row but not final row
                    placed = 1;
                }
            }
        }

        // No placement made - invalid row. Time to backtrack, so we decrement.
        if (placed == 0 && i > 0 && !done) {
            // Decrement until we find a row that we can go back to
            // loop construct's increment will take us forward one.
            int prev_row = i - 1;

            // Find what column is used by previous row, flip the sign if neccessary,
            // then flip that new index to mark it as unused.
            comparisons++;
            int enc_index = arr[prev_row];
            if (enc_index < 0) 
                enc_index = -enc_index - 1;
            if (arr[enc_index] >= 0) {
                init_col = enc_index + 1;
                arr[enc_index] = -arr[enc_index] - 1;
            }

            i = prev_row - 1;  

            if (prev_row == 0 && init_col >= (num + 1) / 2) {
                done = 1;
            }
        } else {
            init_col = 0;
        }
    }

    if (done == 1) {
        printf("No solution for a %d-dimensional board.\n", num);
    } 

    printf("Number of array entry comparisons performed: %d\n", comparisons);
}

int good_diags(int row, int col, int* arr) {
    int i;
    int max_translate = row > col ? row : col;
    for (i = 1; i <= max_translate; i++) {
        comparisons++;
        if (i <= col && i <= row) {
            comparisons++;
            if (arr[row - i] == col - i)
                return 0;
            comparisons++;
            if (arr[row - i] == -col + i - 1)
                return 0;
        }

        if (i + col < num && i <= row) {
            comparisons++;
            if (arr[row - i] == col + i)
                return 0;
            comparisons++;
            if (arr[row - i] == -col - i - 1)
                return 0;
        }
    }
    return 1;
}

void print_forward(int* arr) {
    int i, j;
    printf("A visual solution for a %d-dimensional board: \n", num);
    for (i = 0; i < num; i++) {
        for (j = 0; j < arr[i]; j++)
            printf("* ");
        printf("Q ");
        for (j = arr[i]; j < num - 1; j++)
            printf("* ");
        printf("\n");
    }
    printf("\n");
}

