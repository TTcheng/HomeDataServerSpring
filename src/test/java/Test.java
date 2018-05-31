public class Test {
    public static void main(String[] args) {
        String s = new String("hello");
        String s1 = "hello1";
        s1 = s1.replace("1","");

        System.out.println(s==s1);
        System.out.println(s.equals(s1));
    }
}
