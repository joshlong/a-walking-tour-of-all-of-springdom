package org.springsource.sawt.ioc.basicioc.qualifiers;

public interface BookShop {
  
	long sell(String isbn);
  
	long buy (String isbn);
  
}
