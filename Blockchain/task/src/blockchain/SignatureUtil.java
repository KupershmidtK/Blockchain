package blockchain;

import java.io.File;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignatureUtil {
    private static PublicKey getPublic(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static boolean verifySignature(byte[] data, byte[] signature, byte[] publicKeyBytes) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublic(publicKeyBytes));
        sig.update(data);

        return sig.verify(signature);
    }

    public static byte[] sign(byte[] data, byte[] privatKeyBytes) throws Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privatKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(spec);

        rsa.initSign(privateKey);
        rsa.update(data);
        return rsa.sign();
    }

    public static byte[] getPrivate(String filename) throws Exception {
        return Files.readAllBytes(new File(filename).toPath());
    }

    public static byte[] getPublic(String filename) throws Exception {
        return Files.readAllBytes(new File(filename).toPath());
    }
}
