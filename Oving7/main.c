#include <stdio.h>
#include <string.h>

#define SOURCE 0
#define SINK 7
#define MAX_ITEMS 1000

int parent_vertex[MAX_ITEMS];
int current_route_capacity[MAX_ITEMS];
int edge_capacities[MAX_ITEMS][MAX_ITEMS];
int flow_transferred[MAX_ITEMS][MAX_ITEMS];

typedef struct {
    int constructs[MAX_ITEMS];
    int start_point, end_point, count;
} Queue;

void init_queue(Queue* q){
    q->start_point = -1;
    q->end_point = 0;
    q->count = 0;
};


void add_queue(Queue* q, int new_const){
    q->constructs[++q->start_point] = new_const;
    q->count++;
}

int rm_queue(Queue* q){
    int remove_item = q->constructs[q->end_point++];
    q->count--;
    return remove_item;
}

int fn_smallest(int x, int y){
    return ((x < y) ? x : y);
}

int width_search(){

    memset(parent_vertex, -1, sizeof(parent_vertex));
    memset(current_route_capacity, 0, sizeof(current_route_capacity));

    Queue q;
    init_queue(&q);
    add_queue(&q, SOURCE);

    parent_vertex[SOURCE] = -2;
    current_route_capacity[SOURCE] = MAX_ITEMS - 1;


    while(q.count > 0){
        int current_vertex = rm_queue(&q);

        for (int next = 0; next < MAX_ITEMS; next++) {
            if (parent_vertex[next] == -1) {
                if (edge_capacities[current_vertex][next] - flow_transferred[current_vertex][next] > 0) {
                    parent_vertex[next] = current_vertex;
                    current_route_capacity[next] = fn_smallest(current_route_capacity[current_vertex],
                                                           edge_capacities[current_vertex][next] - flow_transferred[current_vertex][next]);

                    if (next == SINK) {
                        return current_route_capacity[SINK];
                    }

                    add_queue(&q, next);
                }
            }
        }
    }

    return 0;
}
int edmund_karp(){
    int max_flow = 0;

    while(1){
        int flow = width_search();
        if(flow == 0){
            break;
        }

        int present_vertex = SINK;
        int route[MAX_ITEMS];
        int route_size = 0;

        while (present_vertex != SOURCE) {
            int prior_vertex = parent_vertex[present_vertex];
            flow_transferred[prior_vertex][present_vertex] += flow;
            flow_transferred[present_vertex][prior_vertex] -= flow;
            route[route_size++] = present_vertex;
            present_vertex = prior_vertex;
        }
        route[route_size++] = SOURCE;

        printf("%d : ", flow);
        for (int i = route_size - 1; i >= 0; i--) {
            printf("%d ", route[i]);
        }
        printf("\n");

        max_flow += flow;
    }
    return max_flow;
}

int main() {

    FILE* file = fopen("/Users/bergan/Library/CloudStorage/OneDrive-NTNU/DataB2/AlgDatGit/AlgDat/Oving7/flow_1.txt", "r");
    if (file == NULL) {
        printf("Unable to open file.\n");
        return 1;
    }
    int vertex_count, edge_count;
    fscanf(file, "%d %d\n", &vertex_count, &edge_count);

    memset(edge_capacities, 0, sizeof(edge_capacities));
    memset(flow_transferred, 0, sizeof(flow_transferred));

    for(int i = 0; i < edge_count; ++i){
        int from, to, weight;
        fscanf(file, "%d %d %d\n", &from, &to, &weight);
        edge_capacities[from][to] = weight;
    }

    fclose(file);

    if (SINK < 0 || SINK >= vertex_count || SOURCE < 0 || SOURCE >= vertex_count) {
        printf("Invalid source or sink vertex.\n");
        return 1;
    }

    printf("\nFinding max flow vertex %d to vertex %d using Edmund-Karp method\n\n", SOURCE, SINK);

    printf("\nThe max flow from vertex %d to vertex %d is: %d.\n", SOURCE, SINK, edmund_karp());

    return 0;
}
