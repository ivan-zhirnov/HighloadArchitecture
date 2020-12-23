#define _GNU_SOURCE
#include <sched.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/utsname.h>
#include <string.h>

#define STACK_SIZE (1024 * 1024)

static char child_stack[STACK_SIZE];
char str[1001]="";	

// sync primitive
int checkpoint[2];


void print_nodename(){
	struct utsname utsname;
	uname(&utsname);
	printf("%s\n", utsname.nodename);
}

static int child_function() {

printf("\nCurrent hostname: ");
	print_nodename();
	sethostname("Worker",20);
	printf("New hostname: ");
	print_nodename();
    char c;

    // init sync primitive
    close(checkpoint[1]);

    // wait for network setup in parent
    read(checkpoint[0], &c, 1);

    // setup network
    system("ip link set lo up");
    system("ip link set veth1 up");
    system("ip addr add 169.254.1.2/30 dev veth1");

    printf("New NET namespace:\n\n");
    system("ip link list");
    printf("\n");

    // stay it UP to run ping
    
    //sleep(50);

    printf("Child's PID from child_function() from own namespace: %d\n", getpid());
    printf("Parent's PID from child_function() from own namespace: %d\n", getppid());
    system(str); 
    system("mkdir /sys/fs/cgroup/memory/group1");
    system("echo $$ > /sys/fs/cgroup/memory/group1/tasks");
    system("echo 40M > /sys/fs/cgroup/memory/group1/memory.limit_in_bytes");
    return 0;
}

int main() {
    printf("1. Введите относительный путь запускаемой команды (вместе с параметрами): ");
    fgets(str, 1000, stdin);		//путь запускаемой команды
    // init sync primitive
    pipe(checkpoint);
    int flags = CLONE_NEWUTS | CLONE_NEWNS | CLONE_NEWPID | CLONE_NEWNET | SIGCHLD;
    pid_t child_pid = clone(child_function, child_stack + STACK_SIZE, flags, NULL);

    // further init: create a veth pair
    char* cmd;

    asprintf(&cmd, "ip link set veth1 netns %d", child_pid);
    system("ip link add veth0 type veth peer name veth1");
    system(cmd);
    system("ip link set veth0 up");
    system("ip addr add 169.254.1.1/30 dev veth0");
    free(cmd);

    // signal "done"
    close(checkpoint[1]);

    printf("\nOriginal NET namespace:\n\n");
    system("ip link list");
    printf("\n");

    waitpid(child_pid, NULL, 0);
	
    return 0;
}