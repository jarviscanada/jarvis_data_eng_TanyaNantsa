package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc {

    @Override
    public boolean matchJpeg(String filename) {
//        // Splitting the string using a comma as the delimiter
//        String[] splitFilename = filename.split("\\.");
//
//        return splitFilename[1].equalsIgnoreCase("jpg") ||
//                splitFilename[1].equalsIgnoreCase("jpeg");

        String matchingJpeg = "(?i).*\\.(jpeg|jpg)$";
        return filename.matches(matchingJpeg);
    }

    @Override
    public boolean matchIp(String ip) {
//        String[] ipValues = ip.split("\\.");
//        if (ipValues.length != 4) {
//           return false;
//        }
//        for (int i = 0; i < 4; i++) {
//            try {
//                if (Integer.parseInt(ipValues[i]) > 999) {
//                    return false;
//                }
//            } catch (NumberFormatException e) { //catches if not a valid integer
//                return false;
//            }
//        }
//        return true;

        String validIp = "^((\\d|\\d{2}|\\d{3})\\.){3}(\\d|\\d{2}|\\d{3})$";
        return ip.matches(validIp);
    }

    @Override
    public boolean isEmptyLine(String line) {
        String emptyStr = "^\\s*$";
        return line.matches(emptyStr) || line.isEmpty();
    }

    public static void main(String[] args) {
        String filename = "image1.jpeg";
        String filename1 = "image1.jpg";
        String filename2 = "image1.jxx";

        RegexExcImp exampleImp = new RegexExcImp();
        boolean ans1 = exampleImp.matchJpeg(filename);
        boolean ans2 = exampleImp.matchJpeg(filename1);
        boolean ans3 = exampleImp.matchJpeg(filename2);

        System.out.println(ans1);
        System.out.println(ans2);
        System.out.println(ans3);

        System.out.println("----");

        String ip = "0.0.0.0";
        String ip1 = "123.34.556.999";
        String ip2 = "urh.hfg";
        String ip3 = "999.999.999.999";
        String ip4 = "rthr";

        boolean ans4 = exampleImp.matchIp(ip);
        System.out.println(ans4);
        boolean ans5 = exampleImp.matchIp(ip1);
        System.out.println(ans5);
        boolean ans6 = exampleImp.matchIp(ip2);
        System.out.println(ans6);
        boolean ans7 = exampleImp.matchIp(ip3);
        System.out.println(ans7);
        boolean ans8 = exampleImp.matchIp(ip4);
        System.out.println(ans8);

        System.out.println("-----");

        // Test strings
        String str1 = "";       // Empty string
        String str2 = "   ";    // Only spaces
        String str3 = "\t\t";    // Only tabs
        String str4 = "Hello";   // Non-empty string

        boolean ans9 = exampleImp.isEmptyLine(str1);
        boolean ans10 = exampleImp.isEmptyLine(str2);
        boolean ans11 = exampleImp.isEmptyLine(str3);
        boolean ans12 = exampleImp.isEmptyLine(str4);
        System.out.println(ans9);
        System.out.println(ans10);
        System.out.println(ans11);
        System.out.println(ans12);

    }
}
