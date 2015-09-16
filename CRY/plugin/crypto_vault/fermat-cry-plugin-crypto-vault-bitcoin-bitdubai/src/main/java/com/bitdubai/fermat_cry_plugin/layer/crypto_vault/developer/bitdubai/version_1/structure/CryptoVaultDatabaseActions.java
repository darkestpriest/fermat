package com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.event.EventSource;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.ProtocolStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseTransactionFailedException;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events.IncomingCryptoOnCryptoNetworkEvent;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.CantExecuteQueryException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.UnexpectedResultReturnedFromDatabaseException;

import org.bitcoinj.core.Wallet;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by rodrigo on 2015.06.17..
 */
public class CryptoVaultDatabaseActions implements DealsWithEvents, DealsWithErrors{
    /**
     * CryptoVaultDatabaseActions  member variables
     */
    Database database;
    Wallet vault;

    /**
     * DealsWithEvents interface member variables
     */
    EventManager eventManager;

    /**
     * DealsWithErrors interface member variables
     */
    ErrorManager errorManager;


    /**
     * DealsWithErrors interface implementation
     * @param errorManager
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }


    /**
     * Constructor
     * @param database
     */
    public CryptoVaultDatabaseActions(Database database, ErrorManager errorManager, EventManager eventManager){
        this.database = database;
        this.eventManager = eventManager;
        this.errorManager = errorManager;
    }

    public void setVault(Wallet vault){
        this.vault = vault;
    }


    public void saveIncomingTransaction(UUID txId, String txHash) throws CantExecuteQueryException, CantLoadTableToMemoryException {
        /**
         * I need to validate that this is not a transaction I already saved because it might be from a transaction
         * generated by our wallet.
         */
        try {
            DatabaseTable cryptoTxTable;
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_HASH_COLUMN_NAME, txHash, DatabaseFilterType.EQUAL);
            cryptoTxTable.loadToMemory();
            if (cryptoTxTable.getRecords().isEmpty()){
                /**
                 * If this is not a transaction that we previously generated, then I will identify it as a new transaction.
                 */

                    DatabaseTransaction dbTx = this.database.newTransaction();
                    cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
                    DatabaseTableRecord incomingTxRecord =  cryptoTxTable.getEmptyRecord();

                    incomingTxRecord.setUUIDValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId);
                    incomingTxRecord.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_HASH_COLUMN_NAME, txHash);
                    incomingTxRecord.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.toString());
                    incomingTxRecord.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRANSACTION_STS_COLUMN_NAME, CryptoStatus.ON_CRYPTO_NETWORK.toString());

                    dbTx.addRecordToInsert(cryptoTxTable, incomingTxRecord);

                        this.database.executeTransaction(dbTx);

                    /**
                     * after I save the transaction in the database and the vault, I'll raise the incoming transaction.
                     *
                     */

                    FermatEvent event = new IncomingCryptoOnCryptoNetworkEvent(EventType.INCOMING_CRYPTO_ON_CRYPTO_NETWORK);
                    event.setSource(EventSource.CRYPTO_VAULT);
                    eventManager.raiseEvent(event);


            }
        //
        } catch (DatabaseTransactionFailedException e) {
            throw new CantExecuteQueryException("Error in saveIncomingTransaction method.", e, "Transaction Hash:" + txHash, "Error in database plugin.");
        }catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * Validates if the transaction ID passed is new or not. This helps to decide If I need to apply the transactions or not
     * @param txId the ID of the transaction
     * @return
     */
    public boolean isNewFermatTransaction(UUID txId) throws CantExecuteQueryException {
        try {
            DatabaseTable fermatTxTable;

            fermatTxTable = database.getTable(CryptoVaultDatabaseConstants.FERMAT_TRANSACTIONS_TABLE_NAME);
            fermatTxTable.setStringFilter(CryptoVaultDatabaseConstants.FERMAT_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId.toString(), DatabaseFilterType.EQUAL);

                fermatTxTable.loadToMemory();
            /**
             * If I couldnt find any record with this transaction id, then it is a new transactions.
             */
            if (fermatTxTable.getRecords().isEmpty())
                return true;
            else
                return false;
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error validating transaction in DB.", cantLoadTableToMemory, "Transaction Id:" + txId, "Error in database plugin.");
        }catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }


    /**
     * I will persist a new crypto transaction generated by our wallet.
     */
    public  void persistNewTransaction(String txId, String txHash) throws CantExecuteQueryException {
        try {
            DatabaseTable cryptoTxTable;
            DatabaseTransaction dbTx = this.database.newTransaction();
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            DatabaseTableRecord incomingTxRecord =  cryptoTxTable.getEmptyRecord();
            incomingTxRecord.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId.toString());
            incomingTxRecord.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_HASH_COLUMN_NAME, txHash);

            /**
             * since the wallet generated this transaction, we dont need to inform it.
             */
            incomingTxRecord.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME, ProtocolStatus.NO_ACTION_REQUIRED.toString());


            /**
             * The transaction was just generated by us, si it will be saved in PENDING_SUBMIT just in case we are not connected to the network.
             * Then the confidence level will be updated if we were able to send it to the network
             */
            incomingTxRecord.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRANSACTION_STS_COLUMN_NAME, CryptoStatus.ON_CRYPTO_NETWORK.toString());

            dbTx.addRecordToInsert(cryptoTxTable, incomingTxRecord);

            database.executeTransaction(dbTx);

        } catch (DatabaseTransactionFailedException e) {
            throw new CantExecuteQueryException("Error persisting in DB.", e, "Transaction Hash:" + txHash, "Error in database plugin.");
        }catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * Will retrieve all the transactions that are in status pending ProtocolStatus = TO_BE_NOTIFIED
     * @return
     */
    public HashMap<String, String> getPendingTransactionsHeaders() throws CantExecuteQueryException {
        /**
         * I need to obtain all the transactions ids with protocol status SENDING_NOTIFIED y TO_BE_NOTIFIED
         */
        try {
            DatabaseTable cryptoTxTable;
            HashMap<String, String> transactionsIds = new HashMap<String, String>();
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);

            /**
             * I get the transaction IDs and Hashes for the TO_BE_NOTIFIED
             */
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.toString(), DatabaseFilterType.EQUAL);

            cryptoTxTable.loadToMemory();
             for (DatabaseTableRecord record : cryptoTxTable.getRecords()){
                transactionsIds.put(record.getStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME), record.getStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_HASH_COLUMN_NAME));
             }

            return transactionsIds;

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, null, "Error in database plugin.");
        }catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * will update the transaction to the new state
     * @param txHash
     * @param newState
     */
    public void updateCryptoTransactionStatus(String txId, String txHash, CryptoStatus newState) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
        try {
            DatabaseTable cryptoTxTable;
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            DatabaseTableRecord toUpdate=null;
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_HASH_COLUMN_NAME, txHash, DatabaseFilterType.EQUAL);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.FERMAT_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId, DatabaseFilterType.EQUAL);

            cryptoTxTable.loadToMemory();
            if (cryptoTxTable.getRecords().size() > 1)
                throw new UnexpectedResultReturnedFromDatabaseException("Unexpected result. More than value returned.", null, "TxHash:" + txHash + " CryptoStatus:" + newState.toString(), "duplicated Transaction Hash.");
            else
                toUpdate = cryptoTxTable.getRecords().get(0);

            /**
             * I set the Protocol status to the new value
             */
            DatabaseTransaction dbTrx = this.database.newTransaction();
            toUpdate.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRANSACTION_STS_COLUMN_NAME, newState.toString());
            dbTrx.addRecordToUpdate(cryptoTxTable, toUpdate);
            database.executeTransaction(dbTrx);

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, null, "Error in database plugin.");
        } catch (IndexOutOfBoundsException e){
            // I will ignore this because at this point the transaction might not yet be persisted in db.
        } catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * Will update the protocol status of the passed transaction.
     * @param txId
     * @param newStatus
     */
    public void updateTransactionProtocolStatus(UUID  txId, ProtocolStatus newStatus) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
        try {
            DatabaseTable cryptoTxTable;
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId.toString(), DatabaseFilterType.EQUAL);

            cryptoTxTable.loadToMemory();

            DatabaseTransaction dbTrx = this.database.newTransaction();
            DatabaseTableRecord toUpdate=null;
            if (cryptoTxTable.getRecords().size() > 1)
                throw new UnexpectedResultReturnedFromDatabaseException("Unexpected result. More than value returned.", null, "Txid:" + txId+ " Protocol Status:" + newStatus.toString(), "duplicated Transaction Id.");
            else {
                toUpdate = cryptoTxTable.getRecords().get(0);
            }

            /**
             * I set the Protocol status to the new value
             */
            toUpdate.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME, newStatus.toString());
            dbTrx.addRecordToUpdate(cryptoTxTable, toUpdate);
            try {
                database.executeTransaction(dbTrx);
            } catch (DatabaseTransactionFailedException e) {
                throw new CantExecuteQueryException("Error executing query in DB.", e, "TxId " + txId, "Error in database plugin.");
            }
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, "TxId " + txId, "Error in database plugin.");
        }catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * returns the current protocol status of this transaction
     * @param txId
     * @return
     */
    public ProtocolStatus getCurrentTransactionProtocolStatus(UUID txId) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
        try {
            DatabaseTable cryptoTxTable;
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId.toString(), DatabaseFilterType.EQUAL);

            cryptoTxTable.loadToMemory();

            DatabaseTableRecord currentStatus = null;
            /**
             * I will make sure I only get one result.
             */
            if (cryptoTxTable.getRecords().size() > 1)
                throw new UnexpectedResultReturnedFromDatabaseException("Unexpected result. More than value returned.", null, "TxId:" + txId.toString(), "duplicated Transaction Hash.");
            else
                currentStatus = cryptoTxTable.getRecords().get(0);

            return ProtocolStatus.valueOf(currentStatus.getStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME));

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, "TxId " + txId, "Error in database plugin.");
        }catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }


    /**
     * Gets from database the current CryptoStatus of a transaction.
     * @param txId
     * @return
     * @throws CantExecuteQueryException
     * @throws UnexpectedResultReturnedFromDatabaseException
     */
    public CryptoStatus getCryptoStatus (String txId) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
        try {
            DatabaseTable cryptoTxTable;
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId.toString(), DatabaseFilterType.EQUAL);

            cryptoTxTable.loadToMemory();

            DatabaseTableRecord currentRecord = null;
            /**
             * I will make sure I only get one result.
             */
            if (cryptoTxTable.getRecords().size() > 1)
                throw new UnexpectedResultReturnedFromDatabaseException("Unexpected result. More than value returned.", null, "TxId:" + txId.toString(), "duplicated Transaction Hash.");
            else if (cryptoTxTable.getRecords().size() == 0)
                throw new UnexpectedResultReturnedFromDatabaseException("No values returned when trying to get CryptoStatus from transaction in database.", null, "TxId:" + txId.toString(), "transaction not yet persisted in database.");
            else
                currentRecord = cryptoTxTable.getRecords().get(0);

            CryptoStatus cryptoStatus = CryptoStatus.valueOf(currentRecord.getStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRANSACTION_STS_COLUMN_NAME));
            return cryptoStatus;
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, "TxId " + txId, "Error in database plugin.");
        } catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }
    /**
     * will return true if there are transactions in TO_BE_NOTIFIED status
     * @return
     */
    @Deprecated
    public boolean isPendingTransactions() throws CantExecuteQueryException {
        try {
            DatabaseTable cryptoTxTable;
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME,ProtocolStatus.TO_BE_NOTIFIED.toString() ,DatabaseFilterType.EQUAL);
            cryptoTxTable.loadToMemory();

            return !cryptoTxTable.getRecords().isEmpty();

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, null, "Error in database plugin.");
        } catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * Will search for pending transactions to be notified with the passed crypto:_Status
     * @param cryptoStatus
     * @return
     * @throws CantExecuteQueryException
     */
    public boolean isPendingTransactions(CryptoStatus cryptoStatus) throws CantExecuteQueryException {
        try {
            DatabaseTable cryptoTxTable;
            cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME,ProtocolStatus.TO_BE_NOTIFIED.toString() ,DatabaseFilterType.EQUAL);
            cryptoTxTable.setStringFilter(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRANSACTION_STS_COLUMN_NAME,cryptoStatus.toString() ,DatabaseFilterType.EQUAL);

            cryptoTxTable.loadToMemory();

            return !cryptoTxTable.getRecords().isEmpty();

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, null, "Error in database plugin.");
        }catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }


    /**
     * increase by one or resets to zero the counter of transactions found ready to be consumed
     * @param newOcurrence
     * @return the amount of iterations
     * @throws CantExecuteQueryException
     */
    public int updateTransactionProtocolStatus(boolean newOcurrence) throws CantExecuteQueryException {
        try {
            DatabaseTable transactionProtocolStatusTable;
            transactionProtocolStatusTable = database.getTable(CryptoVaultDatabaseConstants.TRANSITION_PROTOCOL_STATUS_TABLE_NAME);

            transactionProtocolStatusTable.loadToMemory();

            List<DatabaseTableRecord> records = transactionProtocolStatusTable.getRecords();
            if (records.isEmpty()) {
                /**
                 * there are no records, I will insert the first one that will be always updated
                 */
                long timestamp = System.currentTimeMillis() / 1000L;
                DatabaseTableRecord emptyRecord = transactionProtocolStatusTable.getEmptyRecord();
                emptyRecord.setLongValue(CryptoVaultDatabaseConstants.TRANSITION_PROTOCOL_STATUS_TABLE_TIMESTAMP_COLUMN_NAME, timestamp);
                emptyRecord.setIntegerValue(CryptoVaultDatabaseConstants.TRANSITION_PROTOCOL_STATUS_TABLE_OCURRENCES_COLUMN_NAME, 0);

                DatabaseTransaction transaction = database.newTransaction();
                transaction.addRecordToInsert(transactionProtocolStatusTable, emptyRecord);

               database.executeTransaction(transaction);

                /**
                 * returns 1
                 */
                return 0;
            }

            DatabaseTableRecord record = records.get(0);
            DatabaseTransaction dbTx = database.newTransaction();

            if (newOcurrence) {
                /**
                 * I need to increase the ocurrences counter by one
                 */
                int ocurrence = record.getIntegerValue(CryptoVaultDatabaseConstants.TRANSITION_PROTOCOL_STATUS_TABLE_OCURRENCES_COLUMN_NAME);
                ocurrence++;
                record.setIntegerValue(CryptoVaultDatabaseConstants.TRANSITION_PROTOCOL_STATUS_TABLE_OCURRENCES_COLUMN_NAME, ocurrence);
                dbTx.addRecordToUpdate(transactionProtocolStatusTable, record);

                database.executeTransaction(dbTx);

                return ocurrence;

            } else {
                /**
                 * I need to reset the counter to 0
                 */
                record.setIntegerValue(CryptoVaultDatabaseConstants.TRANSITION_PROTOCOL_STATUS_TABLE_OCURRENCES_COLUMN_NAME, 0);
                dbTx.addRecordToUpdate(transactionProtocolStatusTable, record);

                database.executeTransaction(dbTx);

                return 0;

            }
            //
        } catch (DatabaseTransactionFailedException e) {
            throw new CantExecuteQueryException("Error executing query in DB.", e, null, "Error in database plugin.");
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            throw new CantExecuteQueryException("Error executing query in DB.", cantLoadTableToMemory, null, "Error in database plugin.");
        } catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * Insert a new Fermat transaction in the database
     * @param txId
     * @throws CantExecuteQueryException
     */
    public void persistnewFermatTransaction(String txId) throws CantExecuteQueryException {
        try {
            DatabaseTable fermatTable;
            fermatTable = database.getTable(CryptoVaultDatabaseConstants.FERMAT_TRANSACTIONS_TABLE_NAME);
            DatabaseTableRecord insert = fermatTable.getEmptyRecord();
            insert.setStringValue(CryptoVaultDatabaseConstants.FERMAT_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, txId);
            DatabaseTransaction dbTx = database.newTransaction();
            dbTx.addRecordToInsert(fermatTable, insert);
            database.executeTransaction(dbTx);
        } catch (DatabaseTransactionFailedException e) {
            throw new CantExecuteQueryException("Error executing query in DB.", e, "TxId: " + txId, "Error in database plugin.");
        } catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /**
     * Insert a new transaction with the confidence level or update an outgoing cryptostatus of an existing transaction.
     * @param hashAsString
     * @param cryptoStatus
     * @throws CantExecuteQueryException
     */
    public void insertNewTransactionWithNewConfidence(String hashAsString, CryptoStatus cryptoStatus) throws CantExecuteQueryException {

        /**
         *  I will insert a new one.
         */
        try {
                DatabaseTable cryptoTxTable = database.getTable(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_NAME);
                DatabaseTableRecord record = cryptoTxTable.getEmptyRecord();
                /**
                 * I generate a new transaction Id
                 */
                record.setStringValue(CryptoVaultDatabaseConstants.FERMAT_TRANSACTIONS_TABLE_TRX_ID_COLUMN_NAME, UUID.randomUUID().toString());
                /**
                 * I use the same transaction hash
                 */
                record.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRX_HASH_COLUMN_NAME, hashAsString);
                record.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_PROTOCOL_STS_COLUMN_NAME, ProtocolStatus.TO_BE_NOTIFIED.toString());
                record.setStringValue(CryptoVaultDatabaseConstants.CRYPTO_TRANSACTIONS_TABLE_TRANSACTION_STS_COLUMN_NAME, cryptoStatus.toString());

                DatabaseTransaction dbTran = database.newTransaction();
                dbTran.addRecordToInsert(cryptoTxTable, record);

                database.executeTransaction(dbTran);

        } catch (DatabaseTransactionFailedException e) {
            throw new CantExecuteQueryException("Error inserting new transaction because of transaction confidence changed.", e, "Transaction Hash:" + hashAsString + " CryptoStatus:" + cryptoStatus.toString(), "Error in database plugin.");
        } catch(Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }
}
