#include <stdio.h>
#include <stdlib.h>

void queens(int);
int good_col(int, int, int*);
int good_diags(int, int, int*);

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
    int done = 0;

    // Initialize the array
    for (i = 0; i < num; i++) 
        arr[i] = 0;

    int init_col = 0;
    // Loop over each row.
    for (i = 0; i < num && done == 0; i++) {
        int placed = 0;
        // Iterate over columns to place queen, starting at 0.
        // 'placed' variable acts as a boolean flag, continue to next row on 1
        for (j = init_col; j < num && placed == 0; j++) {
            // Check previous rows/columns/diagonals. 
            if (good_col(i, j, arr) && good_diags(i, j, arr)) {
                arr[i] = j;
                placed = 1;
            }
        }
        // No placement made - invalid row. Time to backtrack, so we decrement.
        if (placed == 0) {
            // Decrement until we find a row that we can go back to
            // loop construct's increment will take us forward one.
            int prev_row = i - 1;

            // Account for extra comparisons needed at final while-loop iteration
            if (prev_row > 0) 
                comparisons++;

            while (prev_row > 0 && arr[prev_row] == num - 1) {
                comparisons++;
                prev_row--;
            }

            i = prev_row - 1;  
            init_col = arr[prev_row] + 1;

            if (prev_row == 0 && init_col >= (num + 1) / 2) {
                printf("No solution for a %d-dimensional board.\n", num);
                done = 1;
            }
        } else {
            init_col = 0;
        }
    }

    if (done == 0) {
        // Theoretically we made it! Successful solution found.
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

    printf("Number of array entry comparisons performed: %d\n", comparisons);
}

int good_col(int row, int col, int* arr) {
    int i;
    for (i = 0; i < row; i++) {
        comparisons++;
        if (arr[i] == col) {
            return 0;
        }
    }
    return 1;
}

int good_diags(int row, int col, int* arr) {
    int i;
    int max_translate = row > col ? row : col;
    for (i = 1; i <= max_translate; i++) {
        comparisons++;
        if (i <= col && i <= row && arr[row - i] == col - i) {
            return 0;
        } 

        comparisons++;
        if (i + col < num && i <= row && arr[row - i] == col + i) {
            return 0;
        }
    }
    return 1;
}

