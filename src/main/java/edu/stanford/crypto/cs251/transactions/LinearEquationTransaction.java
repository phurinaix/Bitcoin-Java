package edu.stanford.crypto.cs251.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.io.File;
import java.math.BigInteger;
import java.net.UnknownHostException;

import static org.bitcoinj.script.ScriptOpCodes.*;

/**
 * Created by bbuenz on 24.09.15.
 */
public class LinearEquationTransaction extends ScriptTransaction {
    // TODO: Problem 2
    public LinearEquationTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
    }

    @Override
    public Script createInputScript() {
        // TODO: Create a script that can be spend by two numbers x and y such that x+y=first 4 digits of your suid and |x-y|=last 4 digits of your suid (perhaps +1)
    	ScriptBuilder builder = new ScriptBuilder();
    	
    	// Thammasat Student number = 5810742139
    	BigInteger firstAndThirdDigits = new BigInteger("51");
    	BigInteger lastTwoDigits = new BigInteger("39");
        builder.op(OP_2DUP);
        builder.op(OP_ADD);
        builder.data(encode(firstAndThirdDigits));
        builder.op(OP_EQUALVERIFY);
        builder.op(OP_SUB);
        builder.data(encode(lastTwoDigits));
        builder.op(OP_EQUAL);
        return builder.build();
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedScript) {
        // TODO: Create a spending script
    	ScriptBuilder builder = new ScriptBuilder();
        BigInteger x = new BigInteger("45");
        BigInteger y = new BigInteger("6");
        builder.data(encode(x));
        builder.data(encode(y));
        return builder.build();
    }

    private byte[] encode(BigInteger bigInteger) {
        return Utils.reverseBytes(Utils.encodeMPI(bigInteger, false));
    }
}
