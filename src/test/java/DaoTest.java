import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import Common.FactoryDao;
import Common.Util;
import Dao.LocalizationDao;
import Dao.LowCategoryDao;
import Dao.MenuDao;
import Dao.PaymentDao;
import Model.Localization;
import Model.LowCategory;
import Model.Menu;

public class DaoTest {
	public static void main(String... arg) {
		String aa = "aaa";
		String bb = null;
		System.out.println(aa +bb);
		/*LowCategory lc =  FactoryDao.getDao(LowCategoryDao.class).getLowCategory("i");
		Number a = FactoryDao.getDao(PaymentDao.class).getFullTotal(lc);
		System.out.println(a);*/
		/*List<Menu> list = FactoryDao.getDao(MenuDao.class).getData();
		for (Menu item : list) {
			System.out.println(item.getName());
		}
		Optional<Localization> data = FactoryDao.getDao(LocalizationDao.class).getData().stream()
				.filter(x -> Util.StringEquals(x.getId().getKey(), "Expenditure") && Util.StringEquals(x.getId().getType(), "0")).findFirst();
		if(!data.isPresent()) {
			System.out.println("empty");
		}else {
			System.out.println(data.get().getValue());
			
		}*/
	}
}
