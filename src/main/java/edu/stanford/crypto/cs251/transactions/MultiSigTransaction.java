package edu.stanford.crypto.cs251.transactions;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.io.File;
import java.math.BigInteger;

import static org.bitcoinj.script.ScriptOpCodes.*;

/**
 * Created by bbuenz on 24.09.15.
 */
public class MultiSigTransaction extends ScriptTransaction {
    // TODO: Problem 3
	private ECKey bank;
    private ECKey customerA;
    private ECKey customerB;
    private ECKey customerC;
    
    public MultiSigTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
        bank = randKey();
        customerA = randKey();
        customerB = randKey();
        customerC = randKey();
    }

    @Override
    public Script createInputScript() {
        // TODO: Create a script that can be spend using signatures from the bank and one of the customers
    	ScriptBuilder builder = new ScriptBuilder();
        
        builder.data(bank.getPubKey());
        builder.op(OP_CHECKSIGVERIFY);
        builder.op(OP_1);
        builder.data(customerA.getPubKey());
        builder.data(customerB.getPubKey());
        builder.data(customerC.getPubKey());
        builder.op(OP_3);
        builder.op(OP_CHECKMULTISIG);
        return builder.build();
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedTransaction) {
        // Please be aware of the CHECK_MULTISIG bug!
        // TODO: Create a spending script
    	ScriptBuilder builder = new ScriptBuilder();
        TransactionSignature b = sign(unsignedTransaction, bank);
        TransactionSignature cA = sign(unsignedTransaction, customerA);
        TransactionSignature cB = sign(unsignedTransaction, customerB);
        TransactionSignature cC = sign(unsignedTransaction, customerC);

        builder.op(OP_2);   //to resolve the CHECK_MULTISIG bug
        builder.data(cA.encodeToBitcoin());
        builder.data(b.encodeToBitcoin());
        return builder.build();
    }
}
