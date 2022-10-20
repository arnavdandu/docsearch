import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileHelpers {
    static List<File> getFiles(Path start) throws IOException {
        File f = start.toFile();
        List<File> result = new ArrayList<>();
        if (f.isDirectory()) {
            System.out.println("It's a folder");
            File[] paths = f.listFiles();
            for (File subFile : paths) {
                result.addAll(getFiles(subFile.toPath()));
            }
        } else {
            result.add(start.toFile());
        }
        return result;
    }

    static String readFile(File f) throws IOException {
        //System.out.println(f.toString());
        //System.out.println(new String(Files.readAllBytes(f.toPath())));
        return new String(Files.readAllBytes(f.toPath()));
    }
}

class Handler implements URLHandler {
    List<File> files;

    Handler(String directory) throws IOException {
        System.out.println(directory);
        this.files = FileHelpers.getFiles(Paths.get(directory));
    }

    private String listToString(List<File> l) {
        String result = "";
        for (File f : l) {
            result += f.toString() + "\n";
        }
        return result;
    }

    public String handleRequest(URI url) throws IOException {
        int count = 0;
        List<File> matches = new ArrayList<>();
        if (url.getPath().equals("/")) {
            return "There are " + files.size() + " files to search";
        } else if (url.getPath().contains("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                System.out.println(parameters[1]);
                for (File f : files) {
                    if (FileHelpers.readFile(f).contains(parameters[1])) {
                        count++;
                        matches.add(f);
                    }
                }
                return "There were " + count + " files found:\n" + listToString(matches);
            }
        }
        return "404: Page not found";
    }
}

class DocSearchServer {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler("./technical/"));
    }
}
