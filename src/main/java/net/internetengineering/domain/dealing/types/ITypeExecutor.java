package net.internetengineering.domain.dealing.types;

import net.internetengineering.domain.dealing.BuyingOffer;
import net.internetengineering.domain.dealing.SellingOffer;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import net.internetengineering.exception.DBException;


public interface ITypeExecutor {
	
	public void sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers,
                List<BuyingOffer> buyingOffers, String symbol, Connection dbConnection) throws DBException,SQLException;
	public void buyingExecute(PrintWriter out, BuyingOffer offer, List<SellingOffer> sellingOffers,
                List<BuyingOffer> buyingOffers, String symbol,Connection dbConnection) throws DBException,SQLException;
	
}
