#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <string.h>

#define MAX_PATH 4069

struct kant_st;
typedef struct node_st{
    struct kant_st *kant1;
    char character; //represents the single character
    int count; //represents the amount of characters
} Node;


typedef struct kant_st{
    struct kant *neste;
    struct node_st startNode;
    struct node_st sluttNode;
} Kant;

typedef struct{
    int N, K;
    Node *node;
}Graf;



int main() {

    FILE* file = fopen("flow1", "r");
    //if (file == NULL) {
    //   printf("Unable to open file.\n");
    //   return 1;
    char word[] = "rabarbra";

    for (int i = 0; i < strlen(word), i++){

    }
    return 0;

}
/lempel -> hoffman