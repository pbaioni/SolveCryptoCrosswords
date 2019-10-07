import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import app.model.helper.GridHelper;

public class KeysTest {
	
	@Test
	public void testCompatibility() {

		String key1 = "??????????????????????????";
		String key2 = "??????????????????????????";
		assertTrue(GridHelper.areCompatibleKeys(key1, key2));
		
		String key3 = "a?????????????????????????";
		String key4 = "a?????????????????????????";
		assertTrue(GridHelper.areCompatibleKeys(key3, key4));
		
		String key5 = "a???????g?????????????????";
		String key6 = "a????????h????????????????";
		assertTrue(GridHelper.areCompatibleKeys(key5, key6));
		
		String key7 = "a???????g?????o???????????";
		String key8 = "a????????h????p???????????";
		assertFalse(GridHelper.areCompatibleKeys(key7, key8));
	
	}
	
	@Test
	public void margeTest1() {

		String key1   = "??????????????????????????";
		List<String> list = new ArrayList<String>();
		list.add(key1);
		String key2   = "??????????????????????????";
		String merged = "??????????????????????????";
		assertTrue(merged.equals(GridHelper.mergeKeys(list, key2).get(0)));
	
	}
	
	@Test
	public void margeTest2() {

		String key1   = "a?????????????????????????";
		String key2   = "?g????????????????????????";
		String merged = "ag????????????????????????";
		List<String> list = new ArrayList<String>();
		list.add(key1);

		assertTrue(merged.equals(GridHelper.mergeKeys(list, key2).get(0)));
	
	}
	
	@Test
	public void margeTest3() {

		String key1   = "a???????h????????????u???r";
		String key2   = "?g??????h???????y????????r";
		String merged = "ag??????h???????y????u???r";
		List<String> list = new ArrayList<String>();
		list.add(key1);

		assertTrue(merged.equals(GridHelper.mergeKeys(list, key2).get(0)));
	
	}
	

}
