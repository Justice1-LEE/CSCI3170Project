
public class test {
    public static void main(String[] args) {
        String line = "User00000003	james cheng	35	professor	3";
        String[] att = line.split("\t");
        System.out.println(att[0]);
        System.out.println(att[1]);
        System.out.println(att[2]);
        System.out.println(att[3]);
        System.out.println(att[4]);
    }
}
