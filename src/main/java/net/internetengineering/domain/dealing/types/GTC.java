package net.internetengineering.domain.dealing.types;

import net.internetengineering.domain.dealing.BuyingOffer;
import net.internetengineering.domain.dealing.Instrument;
import net.internetengineering.domain.dealing.SellingOffer;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import net.internetengineering.exception.DBException;

public class GTC implements ITypeExecutor {

	@Override
	public void sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers, String symbol, Connection dbConnection) throws DBException,SQLException{
		sellingOffers.add(offer);
		Instrument.sortOfferingListByPrice(sellingOffers);
		if (buyingOffers.isEmpty())
			out.println("Order is queued");
		else
			Instrument.matchingOffers(out, true,sellingOffers,buyingOffers,symbol,this.getClass().getName(),dbConnection);
	}

	@Override
	public void buyingExecute(PrintWriter out, BuyingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers, String symbol, Connection dbConnection)throws DBException,SQLException {
		buyingOffers.add(offer);
		Instrument.sortOfferingListByPrice(buyingOffers);
		if (sellingOffers.isEmpty())
			out.println("Order is queued");
		else
			Instrument.matchingOffers(out, true,sellingOffers,buyingOffers,symbol,this.getClass().getName(),dbConnection);
	}

	
}
