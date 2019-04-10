/*
 * Purpose: Design and Analysis of Algorithms Assignment 1 Problem 2 extended (no output)
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
void print_backward(int*);
// void print_partial(int*, int);

int num;
int comparisons = 0;
int solutions = 0;

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
    int done = 0;

    // Initialize the arrays
    for (i = 0; i < num; i++) {
        arr[i] = -1;
    }

    int init_col = 0;
    // Loop over each row.
    for (i = 0; i < num && done == 0; i++) {
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
                // Last row, successful board. Note we don't set placed to 1
                // so we can keep searching for more solutions.
                if (i == num - 1) {
                    solutions++;
//                    print_forward(arr);

                    // If n > 1, we can flip the board horizontally for another sol.
                    // We don't automatically flip ones with the initial column in the
                    // middle because those will still be searched through
                    // shold be able to remove num > 1 condition because it's middle too
                    if (num > 1 && num / 2 != arr[0]) {
                        solutions++;
  //                      print_backward(arr);
                    }

                    // Clear the last column. All positions are positive, so we don't need to check.
                    arr[j] = -arr[j] - 1;
                } else { // Successful row but not final row
                    placed = 1;
                }
            }
        }

        // No placement made - invalid row. Time to backtrack, so we decrement.
        if (placed == 0 && i > 0) {
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

    if (solutions == 0) {
        printf("No solution for a %d-dimensional board.\n", num);
    } else {
        printf("%d solutions have been found\n", solutions);
    }

    printf("Number of array entry comparisons performed: %d\n", comparisons);
}

/*
int good_diags(int row, int col, int* arr) {
    int i;
    int max_translate = row > col ? row : col;
    for (i = 1; i <= max_translate; i++) {
        comparisons++;
        if (i <= col && i <= row && 
            (arr[row - i] == col - i || arr[row - i] == -col + i - 1)) {
            return 0;
        } 

        comparisons++;
        if (i + col < num && i <= row && 
            (arr[row - i] == col + i || arr[row - i] == -col - i - 1)) {
            return 0;
        }
    }
    return 1;
}
*/

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
    printf("Visual solution %d for a %d-dimensional board: \n", 
            solutions, num);
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

void print_backward(int* arr) {
    int i, j;
    printf("Visual solution %d for a %d-dimensional board: \n", 
            solutions, num);
    for (i = 0; i < num; i++) {
        for (j = arr[i]; j < num - 1; j++)
            printf("* ");
        printf("Q ");
        for (j = 0; j < arr[i]; j++)
            printf("* ");
        printf("\n");
    }
    printf("\n");
}

// Testing method only.
/*
void print_partial(int* arr, int rows) {
    int i, j;
    printf("Visual solution %d for a %d-dimensional board: \n", 
            solutions, num);
    for (i = 0; i < rows; i++) {
        printf("\t");
        int col = arr[i] > 0 ? arr[i] : -arr[i] - 1;
        for (j = 0; j < col; j++)
            printf("* ");
        printf("Q ");
        for (j = col; j < num - 1; j++)
            printf("* ");
        printf("\n");
    }
    for (i = 0; i < num - rows; i++) {
        printf("\t");
        for (j = 0; j < num; j++)
            printf("* ");
        printf("\n");
    }
    printf("\n");
}
*/
