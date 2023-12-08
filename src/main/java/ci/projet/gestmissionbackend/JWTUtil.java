package ci.projet.gestmissionbackend;


public class JWTUtil {
    public static final String SECRET="mySecret12345";
    public static final String AUTH_HEADER="Authorization";
    public static final String PREFIX="Bearer ";
    public static final long EXPIRE_ACCESS_TOKEN=10*60*4000;
    public static final long EXPIRE_REFRESH_TOKEN=15*60*1000;

}
