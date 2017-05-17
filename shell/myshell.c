#include<stdio.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<stdlib.h>
#include<string.h>
#include<wait.h>
#include<fcntl.h>
#include<errno.h>
#include<signal.h>

#define MAXNAME 100 
#define PIPELINE 20
#define MAXARG 20
#define OPEN_MAX 20
#define MAXLINE 200
#define MAXDIR 50
#define MAXSAVE  100
#define MAXLEN   50

#define FALSE 0
#define TRUE 1

#define ERROR 0
#define OKAY 1
#define QUIT -1

////////////////////*******struct of command*******////////////////////////

struct cmd{
    int infd;                 //input file
    int outfd;                //output file
    char* av[MAXARG];         //parameter
}cmdlin[PIPELINE];


struct cmd historyCmd[MAXSAVE]; //history command list

//////////*********parameters*********//////////////
int hisNum=0;                 
int lastpid;
int backgnd;
char *lineptr;
char *avptr;
char infile[MAXNAME+1];
char outfile[MAXNAME+1];
char avline[MAXLINE+1];
char line[MAXLINE+1];   	
int append;
char curdir[MAXDIR];

/////////////////********functions********////////////////////
void command(int i);              //translate input into command + parameter
void execute(int i);              //handle command(upper)
void forkexec(struct cmd* ptr);   //handle pointed command 
int check(char* ptr);             //find pointed command
void getname(char* name);         //store file name
int parse();                      //translate input into command + parameter（upper）
int getinline();                  //get and store input
void initial();                   //initializatoin
void addCmd(struct cmd newCmd);   //add history command
void printHistory();              //print history

//////////////************handle ctr+C***********/////////
void handle_SIGINT()
{
	printf("\n***********command history***********\n");
	printHistory();
	return;
}

/////////////**************main function****************////////////
main()
{
	signal(SIGINT,handle_SIGINT);
	int x,z;
     for(z=0;z<MAXSAVE;z++)
     {
        for(x=0;x<MAXARG;x++)
        {  
              historyCmd[z].av[x]=malloc(MAXLEN);
        }
     }

    int k;
	for(;;)
	{
	    initial();                    //initialization
		int s=getinline();      //get input  
		if(s==QUIT)
		break;                   //exit
   		
		if(s==OKAY)              //handle command
		{
	 		if(k=parse()) 
        	execute(k);
		}
    
    	if(s==ERROR)			//erro
         	 continue; 
	}
	
     for(z=0;z<MAXSAVE;z++)
     {
        for(x=0;x<MAXARG;x++)
        {  
              free(historyCmd[z].av[x]);
        }
     }
}


/////////////************initialiazation************////////////////
void initial()
{
    int i;
	backgnd=FALSE;
	lineptr=line;
	avptr=avline;
	infile[0]='\0';
	outfile[0]='\0';
	append=FALSE;
	
    for(i=0;i<PIPELINE;++i)
	{
	    cmdlin[i].infd = 0;
		cmdlin[i].outfd = 1;
	}
	for(i=3;i<OPEN_MAX;++i)
	    close(i);
      
	getcwd(curdir,MAXDIR);                //get directory
	printf("[wuhao-shell@ %s:]#",curdir);

	fflush(stdout);
}

////////////***********get input************/////////////
int getinline()
{

	int i;
       
	for(i=0;(line[i]=getchar())!='\n'&&i<MAXLINE;++i); 
	if(i==MAXLINE)
	{
	    fprintf(stderr,"Command line too long\n");
	    return(ERROR);
	}
  
	line[i+1]='\0';
	if(strcmp(line,"exit\n")==0) return QUIT;
	return(OKAY);

}
//////////////************translate command(upper)************////////////////
int parse()
{
    int i;
	
	command(0);
    if(check("<"))
	  getname(infile);
	for(i=1;i<PIPELINE;++i)
	    if(check("|"))
		   command(i);
		else
		   break;
	
	if(check(">"))
	{
	   if(check(">"))
	      append=TRUE;        
	    getname(outfile);
	}

	
	if(check("&"))
	   backgnd=TRUE;
 
	if(check("\n"))
	   return(i);
	else
	{
	  fprintf(stderr,"Command line syntax error\n");
	  return(ERROR);
	}

}
//////////**********translate input into command + parameter**********///////////
void command(int i)
{
    int j,flag,inword;

	for(j=0;j<MAXARG-1;++j)
	{
	   while(*lineptr==' '||*lineptr=='\t')
	        ++lineptr;
	   cmdlin[i].av[j]=avptr;
	   cmdlin[i].av[j+1]=NULL;
		for(flag=0;flag==0;)
		{
		    switch(*lineptr)
			{
			    case '>':
				case '<':
				case '|':
				case '&':
				case '\n':
				     if(inword==FALSE)
					    cmdlin[i].av[j]=NULL;
					 *avptr++='\0';
					 return;
				case' ':
				case'\t':
				    inword=FALSE;
					*avptr++='\0';
					flag=1;
					break;
				default:
				    inword=TRUE;
					*avptr++=*lineptr++;
					break;
			}
		}
	}
}

/////////////***************handle command(upper)******************//////////////
void execute(int j)
{
    int i,fd,fds[2];
		
	if(infile[0]!='\0')
	   cmdlin[0].infd=open(infile,O_RDONLY);
	
	if(outfile[0]!='\0')
	   if(append==FALSE)
	      cmdlin[j-1].outfd=open(outfile,O_WRONLY|O_CREAT|O_TRUNC,0666);
	    else
	      cmdlin[j-1].outfd=open(outfile,O_WRONLY|O_CREAT|O_APPEND,0666);

	if(backgnd==TRUE)
	  signal(SIGCHLD,SIG_IGN);
	else
	  signal(SIGCHLD,SIG_DFL);

    if(strcmp(cmdlin[0].av[0],"history")==0)
	{
		printHistory();
		return;
	}
 	
	for(i=0;i<j;++i)
	{
	   
	   if(i<j-1)
	   {
	      pipe(fds);
		  cmdlin[i].outfd=fds[1];
		  cmdlin[i+1].infd=fds[0];
		
	   }
       if(strcmp(cmdlin[i].av[0],"cd")==0)              //cd 
       {
			if((cmdlin[i].av[1])&&chdir(cmdlin[i].av[1]))
			{
				printf("cd:error!\n");
				return;
			}
            addCmd(cmdlin[i]);
            return;
		}
		if(cmdlin[i].av[0][0]=='r')                     //history-cd
		{
        	if(cmdlin[i].av[0][1]=='\0')
            {
				printf("r:cannot find this command!\n");
				return; 
		  	}
           
           	int dex=cmdlin[i].av[0][1]-'0';
           	if(cmdlin[i].av[0][2]!='\0')
           	{
               	int dex1=cmdlin[i].av[0][2]-'0';    
              	dex=dex*10+dex1; 
           	}
            if(dex>hisNum||dex<=0)
			{
				printf("r:cannot execute this command!\n");return;
			}     
            
            if(strcmp(historyCmd[hisNum-dex].av[0],"cd")==0)       //history cd       
            {
                if((historyCmd[hisNum-dex].av[1])&&chdir(historyCmd[hisNum-dex].av[1])) 
				{
					printf("cd:error!\n"); 
					return;
				}
               return;
	   		}
		}
	   	forkexec(&cmdlin[i]);

		if(cmdlin[i].av[0][0]!='r')
           	addCmd(cmdlin[i]);
	   	  
	  	if((fd=cmdlin[i].infd)!=0)
	   		close(fd);
		if((fd=cmdlin[i].outfd)!=1)
	      	close(fd);
	}
	if(backgnd==FALSE)
	while(wait(NULL)!=lastpid);
}

///////////////**************handle pointed command**********///////////////////
void forkexec(struct cmd* ptr)
{
    int pid;
    int i;
		
	if(pid=fork())
	{ 
		if(backgnd==TRUE)
	    printf("%d\n",pid);
	 	lastpid=pid;
	}
	else
	{
		if(ptr->infd==0&&backgnd==TRUE)
	    	ptr->infd=open("/dev/null",O_RDONLY);
	 
	 	if(ptr->infd!=0)
	  	{
	    	close(0);
	    	dup(ptr->infd);
	  	}
	  	if(ptr->outfd!=1)
	  	{
	    	close(1);
	    	dup(ptr->outfd);
	  	}
	  	

		if(backgnd==FALSE)
	  	{
	   		signal(SIGINT,SIG_DFL);
	   		signal(SIGQUIT,SIG_DFL);
	  	}

	  
	  	for(i=3;i<OPEN_MAX;++i)
	  	{
	  		close(i);
		}
      	if(ptr->av[0][0]=='r')                            //history
      	{
          	int index=ptr->av[0][1]-'0';
         	//int dex=ptr->av[0][1]-'0';
          	if(ptr->av[0][2]!='\0')
            {
               int dex1=ptr->av[0][2]-'0';    
               index=index*10+dex1; 
            }         
                
           execvp(historyCmd[hisNum-index].av[0],historyCmd[hisNum-index].av);
           exit(1);
      	}    
     	if(execvp(ptr->av[0],ptr->av)==-1)
      	{
       	printf("%s:Cannot find this comond!\n",ptr->av[0]);
       	exit(1);
      	}
    }
}

/////////////*************find pointed command***************////////////////
int check(char* ptr)
{
    char* tptr;
	while(*lineptr==' ')
	   lineptr++;
	tptr=lineptr;
	while(*ptr!='\0'&&*ptr==*tptr)
	{
	   ptr++;
	   tptr++;
	}
	if(*ptr!='\0')
	   return(FALSE);
	else
	{
	  lineptr=tptr;
	  return(TRUE);
	}
}

////////////////**********resolve command**********////////////////////
void getname(char* name)
{
    int i;
	for(i=0;i<MAXNAME;++i)
	{
	   switch(*lineptr)
	   {

	     case '>':
		 case '<':
		 case '|':
		 case '&':
		 case ' ':
		 case '\t':
		 case '\n':
		      *name='\0';
			  return;
		default:
		      *name++=*lineptr++;
			  break;
	   }
	}
	*name='\0';
}

///////////////////***********add history command**********//////////////////
void addCmd(struct cmd newCmd)
{
   int s;
   char* p;
   char* tp;

   if(hisNum<MAXSAVE)
   {
   for(s=0;newCmd.av[s]!=NULL;s++)
   {
	   tp=historyCmd[hisNum].av[s];
	   p=newCmd.av[s];
	   while(*p!='\0')
	   {
            *tp++=*p++;
	   }
	   *tp='\0';
    }
  for(;s<MAXARG;s++)
     historyCmd[hisNum].av[s]=NULL;
  hisNum++;
     
  }
  else
    printf("Cannot save so much!\n");
}

/////////////////**********print history list**********/////////////////
void printHistory()
{
    int k;
    int ps;
    for(k=hisNum;k>=hisNum-9 && k>=1;k--)
	{
		printf("%d:",k);
		ps=0;
		while(historyCmd[k-1].av[ps]!=NULL)
		{
            printf("%s ",historyCmd[k-1].av[ps++]);
			printf("\n");
		}
	}
	return;
}

