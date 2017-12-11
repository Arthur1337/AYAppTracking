package connectors.user;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserTest {


    public static void main(String[] args){
        String email = "ay.rockz666@gmail.com";
        String passw = "AppYourself123%";
        String randomId="616af60f-c287-402c-b935-3af23743cb89";
       // String userEntityWithoutChat= "zoGSlXFr5xU2gXE5g8cBdvLCkM13";
       // String userEntityWithChat="nhp1vSjAufUZvZ77zO3X02IcwYq2";
        String userEntity = "u80zb1BckyOBpDflTn4RCjYsvgT2";
        //UserDetails userDetails1 = new UserDetails(email,passw,randomId,userEntityWithoutChat);




       // System.out.println(userDetails1.medianAppResponseTime);

        UserDetails userDetails2 = new UserDetails(email,passw,randomId,userEntity);
        if(!userDetails2.connectionFailed())
            System.out.println(userDetails2.isPushAudient);


    }
}
