package com.lele.common;

import java.util.ArrayList;
import java.util.List;

import com.lele.entity.Cart;

public class MJWinValidation {

	public static boolean isWin(List<Cart> carts) {
		switch (carts.size()) {
		case 2:
			if (checkAA(carts)) {
				return true;
			}
			break;

		case 5:
			if (checkAA(getCarts(carts, 0, 1))) {
				if (check3(getCarts(carts, 2, 3, 4))) {
					return true;
				}
			}

			if (checkAAA(getCarts(carts, 1, 2, 3))) {
				if (checkABC(getCarts(carts, 0, 3, 4))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 3, 4))) {
				if (check3(getCarts(carts, 0, 1, 2))) {
					return true;
				}
			}
			break;
		case 8:
			if (checkAA(getCarts(carts, 0, 1))) {
				if (check6(getCarts(carts, 2, 3, 4, 5, 6, 7))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 3, 4))) {
				if (check3(getCarts(carts, 0, 1, 2)) && check3(getCarts(carts, 5, 6, 7))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 6, 7))) {
				if (check6(getCarts(carts, 0, 1, 2, 3, 4, 5))) {
					return true;
				}
			}

			break;

		case 11:
			if (checkAA(getCarts(carts, 0, 1))) {
				if (check9(getCarts(carts, 2, 3, 4, 5, 6, 7, 8, 9, 10))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 3, 4))) {
				if (check3(getCarts(carts, 0, 1, 2)) && check6(getCarts(carts, 5, 6, 7, 8, 9, 10))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 6, 7))) {
				if (check3(getCarts(carts, 8, 9, 10)) && check6(getCarts(carts, 0, 1, 2, 3, 4, 5))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 9, 10))) {
				if (check9(getCarts(carts, 0, 1, 2, 3, 4, 5, 6, 7, 8))) {
					return true;
				}
			}

			break;

		case 14:
			if (checkAA(getCarts(carts, 0, 1))) {
				if (check12(getCarts(carts, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 3, 4))) {
				if (check3(getCarts(carts, 0, 1, 2)) && check9(getCarts(carts, 5, 6, 7, 8, 9, 10, 11, 12, 13))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 6, 7))) {
				if (check6(getCarts(carts, 0, 1, 2, 3, 4, 5)) && check6(getCarts(carts, 8, 9, 10, 11, 12, 13))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 9, 10))) {
				if (check3(getCarts(carts, 11, 12, 13)) && check9(getCarts(carts, 0, 1, 2, 3, 4, 5, 6, 7, 8))) {
					return true;
				}
			}

			if (checkAA(getCarts(carts, 12, 13))) {
				if (check12(getCarts(carts, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
						&& check9(getCarts(carts, 0, 1, 2, 3, 4, 5, 6, 7, 8))) {
					return true;
				}
			}

			break;
		default:
			break;
		}
		return false;
	}

	private static boolean check12(List<Cart> carts) {
		if (checkABC(getCarts(carts, 0, 1, 2)) && check9(getCarts(carts, 3, 4, 5, 6, 7, 8, 9, 10, 11))) {
			return true;
		}

		if (checkAAA(getCarts(carts, 0, 1, 2)) && check9(getCarts(carts, 3, 4, 5, 6, 7, 8, 9, 10, 11))) {
			return true;
		}

		if (checkABC(getCarts(carts, 9, 10, 11)) && check9(getCarts(carts, 0, 1, 2, 3, 4, 5, 6, 7, 8))) {
			return true;
		}

		if (checkAAA(getCarts(carts, 9, 10, 11)) && check9(getCarts(carts, 0, 1, 2, 3, 4, 5, 6, 7, 8))) {
			return true;
		}

		if (check6(getCarts(carts, 0, 1, 2, 3, 4, 5)) && check6(getCarts(carts, 6, 7, 8, 9, 10, 11))) {
			return true;
		}
		return false;
	}

	private static boolean check9(List<Cart> carts) {
		if (checkABC(getCarts(carts, 0, 1, 2)) && check6(getCarts(carts, 3, 4, 5, 6, 7, 8))) {
			return true;
		}

		if (checkAAA(getCarts(carts, 0, 1, 2)) && check6(getCarts(carts, 3, 4, 5, 6, 7, 8))) {
			return true;
		}

		if (checkABC(getCarts(carts, 6, 7, 8)) && check6(getCarts(carts, 0, 1, 2, 3, 4, 5))) {
			return true;
		}

		if (checkAAA(getCarts(carts, 6, 7, 8)) && check6(getCarts(carts, 0, 1, 2, 3, 4, 5))) {
			return true;
		}
		return false;
	}

	private static boolean check6(List<Cart> carts) {
		if (check3(getCarts(carts, 0, 1, 2)) && check3(getCarts(carts, 3, 4, 5))) {
			return true;
		}

		if (check3(getCarts(carts, 0, 1, 3)) && check3(getCarts(carts, 2, 4, 5))) {
			return true;
		}

		if (checkAABBCC(getCarts(carts, 0, 1, 2, 3, 4, 5))) {
			return true;
		}

		if (checkAAAA(getCarts(carts, 1, 2, 3, 4))) {
			if (checkABC(getCarts(carts, 0, 1, 5))) {
				return true;
			}
		}

		return false;
	}

	private static boolean checkAAAA(List<Cart> carts) {
		if (carts.get(0).equals(carts.get(1)) && carts.get(1).equals(carts.get(2)) && carts.get(2).equals(carts.get(3))) {
			return true;
		}
		return false;
	}

	private static boolean checkAABBCC(List<Cart> carts) {
		if (carts.get(0).equals(carts.get(1)) && carts.get(2).equals(carts.get(3)) && carts.get(4).equals(carts.get(5))) {
			if (carts.get(0).getCode() == carts.get(2).getCode() - 1
					&& carts.get(2).getCode() == carts.get(4).getCode() - 1) {
				return true;
			}
		}
		return false;
	}

	private static List<Cart> getCarts(List<Cart> carts, int... index) {
		List<Cart> returnCarts = new ArrayList<Cart>();
		for (int i = 0; i < index.length; i++) {
			returnCarts.add(carts.get(index[i]));
		}
		return returnCarts;
	}

	public static boolean checkAA(List<Cart> carts) {
		if (carts.get(0).equals(carts.get(1))) {
			return true;
		}
		return false;
	}

	public static boolean checkABC(List<Cart> carts) {
		if (carts.get(0).equals(backOne(carts.get(1))) && carts.get(1).equals(backOne(carts.get(2)))) {
			return true;
		}
		return false;
	}

	public static boolean check3(List<Cart> carts) {
		if (checkABC(carts)) {
			return true;
		}
		if (checkAAA(carts)) {
			return true;
		}
		return false;
	}

	private static boolean checkAAA(List<Cart> carts) {
		if (carts.get(0).equals(carts.get(1)) && carts.get(1).equals(carts.get(2))) {
			return true;
		}
		return false;
	}

	private static Cart backOne(Cart cart) {
		return new Cart(cart.getCartType(), cart.getCode() - 1);
	}

}
