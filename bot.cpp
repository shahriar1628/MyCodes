//Bismillahir rahmanir rahim Thanks Allah 4 everything
#include<iostream>
#include<string>
#include<cstdio>
#include<cstdlib>
#include<string>
#include <iostream>
#include <string>
#include <string.h>
#include <queue>
#include <stack>
#include <math.h>
#include <vector>
#include <map>
#include <queue>
#include <utility>
#define inf (1<<29)
#define CITY 25
#define vec vector<int>
#define str vector<string>
#define rep(a,b) for(int a=0;a<b;a++)
#define lp(a,b,c) for(int a=b;a<c;a++)
#define PI 3.14159
#define check(x,y) if(x>39 || x<0 || y>39 || y<0)
#define pp pair<int ,int >
#define pp1 pair<char ,int>
#include <stdio.h>
#include <stdlib.h>
#include <cstdlib>
#include <algorithm>
#include <cstdio>
#include <cstring>
using namespace std;

float getAngle(int x1,int y1,int x2,int y2) {
  //  if(x1==x2) return 0;
   // if(y1==y2) return -1;
    float angle = atan2(y1 -y2, x1 - x2);
    return (angle*180)/(PI *1.0);
}
struct point
{
  int x;
  int y;
  point(int x1,int y1) {
      x=x1;
      y=y1;
  }
  point() {};
};

struct pod
{
  int x;
  int y;
  int vx;
  int vy;
  int angle;
  int nextChekPoint;

  pod(int x1,int y1,int vx1,int vy1,int angle1,int nextChekPoint1) {
      x=x1;
      y=y1;
      vx=vx1;
      vy=vy1;
      angle =angle1;
      nextChekPoint = nextChekPoint1;
  }
    pod() {};

};

point getAnglePoint(point centre, double angle) {
    double y = sin (angle*PI/180)*400;
    double x =cos(angle*PI/180)*400;
   return point(centre.x+x,centre.y-y);

}
double getYValue(int x,double angle,int angle1,int vx) {
    if(vx ==0) vx =1;
    //y=bx-c*x*x
    int cd=angle*0;
    if(cd!=0) angle = angle1;
    cerr << "angelGot" << angle << endl;
    if(angle >180) angle =360-angle;
    double tanTheta ;
    int angle3=(int) angle;
    if(angle3%90==0 && (angle3/90)%2!=0) angle-=10;
     double b =  tanTheta = tan ( angle * PI / 180.0 );

     if(vx ==0) vx = 1;
     double c = (0.85)/(2*vx);
   //  cerr<<b << " " << c << endl;
     return (b*x -c*x*x)*(-1);
}
int  getXValue(point a,point b) {
    int difference = abs(a.x-b.x);
    if(difference <=10) return b.x;
    if(a.x<b.x) return a.x+10;
    return a.x-10;
}
point getLine(point a,point b) {
    return point(b.x-a.x,b.y-a.y);
}
double getAngleTwoVector(point a,point b) {
    double dot = a.x*b.x+a.y*b.y;
    double lenthA = sqrt(a.x*a.x+a.y*a.y);
    double lenthB = sqrt(b.x*b.x+b.y*b.y);
    double param = dot/(lenthA*lenthB);
    return  acos (param) * 180.0 / PI;
}
 double angleBetweenTwoPoints(int point1X,int point1Y,
        int point2X, int point2Y
        ) {
    int  fixedX = 0;
    int  fixedY = 0;
    double angle1 = atan2(point1Y - fixedY, point1X - fixedX);
    double angle2 = atan2(point2Y - fixedY, point2X - fixedX);

    return ((angle1 - angle2)*180)/(PI *1.0);
}
double angleBetweenThreePoints(int point1X,int point1Y,
        int point2X, int point2Y,int fixedX ,int fixedY
        ) {
    double angle1 = atan2(point1Y - fixedY, point1X - fixedX);
    double angle2 = atan2(point2Y - fixedY, point2X - fixedX);

    return ((angle1 - angle2)*180)/(PI *1.0);
}

double get_distance(int x1,int y1,int x2,int y2) {
    return sqrt((y1-y2)*(y1-y2) + (x1-x2)*(x1-x2));
}
double get_distance(point i,point j) {
    return get_distance(i.x,i.y,j.x,j.y) ;
}

bool getShield(pod me,pod enemy1,pod enemy2) {
    point myPod(me.x,me.y);
    point enemyPod1(enemy1.x,enemy1.y);
    point enemyPod2(enemy2.x,enemy2.y);

    double distanceToFirstEnemy = get_distance(myPod,enemyPod1) ;
    double distanceToSecondEnemy = get_distance(myPod,enemyPod2) ;
    if(distanceToFirstEnemy<=900 || distanceToSecondEnemy<=900 ) return true;
    return false;
}



int main()
{
    int laps;
    cin >> laps; cin.ignore();
    int checkPointX[8];
    int checkPointY[8];
    int checkPointList[2];
    int oponentChekPoint[2];
    int changeChekPoint[2] = {0,0} ;
    int oponentX[2];
    int oponentY[2];
    double threashHold[2];
    int saveX[2][3];
    int saveY[2][3];
    pod MyPod[2];
    pod OponentPod[2];
    int checkpointCount;
    int subtractValue = 300;
   //cout << getYValue(3731,63.927,12,-773) << endl;;
    cin >> checkpointCount; cin.ignore();
    for (int i = 0; i < checkpointCount; i++) {
        int checkpointX;
        int checkpointY;
        cin >> checkpointX >> checkpointY; cin.ignore();
        checkPointX[i] = checkpointX;
        checkPointY[i] = checkpointY;
    }

    // game loop
    int countrad = 0;
    while (1) {



        for (int i = 0; i < 2; i++) {
            int x;
            int y;
            int vx;
            int vy;
            int angle;
            int nextCheckPointId;
            cin >> x >> y >> vx >> vy >> angle >> nextCheckPointId;
            pod temp(x,y,vx,vy,angle,nextCheckPointId);
            MyPod[i]=temp;
            if(i==0) {
            //    cerr << "x and y " << x << " " << y  << endl;
              //  cerr << "vx and vy " << vx << " " << vy  << endl;
             //   cerr << "angle" << angle << endl;

            }


            checkPointList[i] = nextCheckPointId;
           //  cerr << "angle  " << angle  << endl;
           //   cerr << "angle between two  " << angleBetweenTwoPoints(x,y,checkPointX[nextCheckPointId],checkPointY[nextCheckPointId])  << endl;
          //   cerr << "angle between main  " << angleBetweenTwoPoints(x,y,10,0)  << endl;
             double angleTwoPoint = angleBetweenTwoPoints(x,y,checkPointX[nextCheckPointId],checkPointY[nextCheckPointId]);
             if(angleTwoPoint < 20) angleTwoPoint =0 ;
             else  if(angleTwoPoint < 40) angleTwoPoint =30 ;
             else if(angleTwoPoint < 80) angleTwoPoint =60 ;
             else angleTwoPoint = 70;
             threashHold[i] = (100)-angleTwoPoint;
             threashHold[i] = 200 ;

        }

        for (int i = 0; i < 2; i++) {
            int x;
            int y;
            int vx;
            int vy;
            int angle;
            int nextCheckPointId;
            cin >> x >> y >> vx >> vy >> angle >> nextCheckPointId; cin.ignore();
             pod temp(x,y,vx,vy,angle,nextCheckPointId);
            OponentPod[i]=temp;
          //  cerr << x << " " << y << "  " << angle << " " <<  nextCheckPointId << endl ;
            double distance2 = getAngle(checkPointX[nextCheckPointId],checkPointY[nextCheckPointId],x,y);
        //    cerr << "distance " << distance2 << endl;
            oponentX[i]=x;
            oponentY[i]=y;
            if(oponentChekPoint[i]!=nextCheckPointId) changeChekPoint[i]++;
            oponentChekPoint[i]=nextCheckPointId;
        }


        point firstPodCenter(MyPod[0].x,MyPod[0].y);
        int firstPodChekPointId = MyPod[0].nextChekPoint;
        point firstPodChekPoint(checkPointX[firstPodChekPointId],checkPointY[firstPodChekPointId]);
        point firstPodChekPointIncreaseX(checkPointX[firstPodChekPointId]+subtractValue,checkPointY[firstPodChekPointId]);
        point firstPodChekPointIncreaseY(checkPointX[firstPodChekPointId],checkPointY[firstPodChekPointId]+subtractValue);

        point AnglePoint = getAnglePoint(firstPodCenter,MyPod[0].angle);
        point angleLine = getLine(firstPodCenter,AnglePoint);
        point destinationLine = getLine(firstPodCenter,firstPodChekPoint);
        point destinationLineIncrX = getLine(firstPodCenter,firstPodChekPointIncreaseX);
        point destinationLineIncrY = getLine(firstPodCenter,firstPodChekPointIncreaseY);

        double angleToDestination1 = getAngleTwoVector(destinationLine,angleLine);
        double angleToDestination2 = getAngleTwoVector(angleLine,destinationLine);
        double angleToDestination = (angleToDestination1 >= angleToDestination2) ? angleToDestination2 : angleToDestination1 ;

        double angleToDestinationX1 = getAngleTwoVector(destinationLineIncrX,angleLine);
        double angleToDestinationX2 = getAngleTwoVector(angleLine,destinationLineIncrX);
        double angleToDestinationX = (angleToDestinationX1 >= angleToDestinationX2) ? angleToDestinationX2 : angleToDestinationX1 ;

        double angleToDestinationY1 = getAngleTwoVector(destinationLineIncrY,angleLine);
        double angleToDestinationY2 = getAngleTwoVector(angleLine,destinationLineIncrY);
        double angleToDestinationY = (angleToDestinationY1 >= angleToDestinationY2) ? angleToDestinationY2 : angleToDestinationY1 ;

        int subtract[2];
        if(angleToDestination>=angleToDestinationX) subtract[0]=subtractValue;
        else subtract[0]=-subtractValue;

        if(angleToDestination>=angleToDestinationY) subtract[1]=subtractValue;
        else subtract[1]=-subtractValue;





       // if(angleToDestination>180) angleToDestination = 360-angleToDestination;
         cerr << "angle found " << angleToDestination << endl;
         int devider = (int) angleToDestination/18;
         if(devider <=0) devider = 1;
       //  cerr << "devider " <<  devider << endl;
         threashHold[0]=200/devider;
         double distanceFromDestination = get_distance(firstPodCenter,firstPodChekPoint);
        // cerr <<  distanceFromDestination << endl;
         if(threashHold[0]<100 && distanceFromDestination >10000) threashHold[0]+=80;
         else if(threashHold[0]<100 && distanceFromDestination >6000) threashHold[0]+=70;
         else if(threashHold[0]<100 && distanceFromDestination >4000) threashHold[0]+=60;
         else if(threashHold[0]<100 && distanceFromDestination >2000) threashHold[0]+=50;

         int MyPodX=firstPodCenter.x;
         int DesX = firstPodChekPoint.x;
        int destinationX = getXValue(firstPodCenter,firstPodChekPoint);
        double destinationY = getYValue(destinationX,angleToDestination,MyPod[0].angle,MyPod[0].vx);
       // cerr << "problem " << destinationY << endl;
        int desY = floor(destinationY);
       cerr << "desx " << destinationX << "des y "  <<  desY << endl;

        // Write an action using cout. DON'T FORGET THE "<< endl"
        // To debug: cerr << "Debug messages..." << endl;

         //if((desY <0 || desY>9000) || (destinationX<0 || destinationX >16000 ) )
            if(getShield(MyPod[0],OponentPod[0],OponentPod[1]))
              cout <<checkPointX[checkPointList[0]] + subtract[0]<< " " << checkPointY[checkPointList[0]] + subtract[1] << " SHIELD"  << " SHIELD" << endl;
           else
           cout <<checkPointX[checkPointList[0]] + subtract[0]<< " " << checkPointY[checkPointList[0]] + subtract[1] << " " << threashHold[0] << " choose first" << endl;
     //   else  cout <<destinationX << " " << desY << " " << "200 " << " !!!!!!!!!!!!!" << endl;
        //  else
            // cout <<checkPointX[checkPointList[0]] << " " << checkPointY[checkPointList[0]] << " 100" <<endl;
        int oponentAtakId ;
        if(changeChekPoint[0] >=changeChekPoint[1]) oponentAtakId = 0;
        else  oponentAtakId = 1;
        cout <<oponentX[oponentAtakId] << " " <<oponentY[oponentAtakId] << " 200"  << endl;
    }
}
