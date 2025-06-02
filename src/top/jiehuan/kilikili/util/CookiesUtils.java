package top.jiehuan.kilikili.util;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class CookiesUtils {
	private RecordStore recordStore;
	public CookiesUtils() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		this("TokenStore");
	}
	public CookiesUtils(String storeName) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		recordStore = RecordStore.openRecordStore(storeName, true);
	}
	public void saveToken(String token) throws RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException {
        byte[] tokenBytes = token.getBytes();  // 将 Token 转为字节数组
        recordStore.addRecord(tokenBytes, 0, tokenBytes.length);  // 保存到 RMS
    }
	public void saveBooleanToken(boolean token) throws RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException{
		byte[] tokenBytes = new byte[] { (byte) (token ? 1 : 0) };  
	    recordStore.addRecord(tokenBytes, 0, tokenBytes.length);
	}
	public void updateToken(String newToken) throws RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException {
            // 检查是否已存在 Token
            if (isTokenStored()) {
                // 删除所有现有记录（如果你想删除所有记录，当然也可以选择删除特定记录）
                RecordEnumeration records = recordStore.enumerateRecords(null, null, false);
                while (records.hasNextElement()) {
                    int recordId = records.nextRecordId();
                    recordStore.deleteRecord(recordId);  // 删除记录
                }

                // 保存新的 Token
                saveToken(newToken);
            } else {
            	saveToken(newToken);
            }
    }

    // 检查是否存在 token
    public boolean isTokenStored() {
        try {
            RecordEnumeration records = recordStore.enumerateRecords(null, null, false);
            return records.hasNextElement();  // 如果有记录，说明 token 存在
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 加载 Token
    public String loadToken() throws InvalidRecordIDException, RecordStoreException {
            RecordEnumeration records = recordStore.enumerateRecords(null, null, false);
            if (records.hasNextElement()) {
                byte[] tokenBytes = records.nextRecord();  // 获取第一个记录（Token）
                String loadedToken = new String(tokenBytes);  // 转换为字符串
                return loadedToken;
            } else {
                return "error";
            }
    }
    
    public void deleteToken() throws RecordStoreException{
    	RecordEnumeration records = recordStore.enumerateRecords(null, null, false);
        while (records.hasNextElement()) {
            int recordId = records.nextRecordId();
            recordStore.deleteRecord(recordId);  // 删除记录
        }
    }
}
