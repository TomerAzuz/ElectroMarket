//import React, { useContext } from 'react';
//import { toast } from 'react-hot-toast';

//import { CartContext } from "../contexts/CartContext";
//import { AuthContext } from "../contexts/AuthContext";

const CartPage = () => {    
   // const { cart, clearCart, total, itemQuantity, handleCheckout } = useContext(CartContext);
    //const { user } = useContext(AuthContext);
    
    return (
        <div className="text-center text-xl mt-36">
            SHOPPING CART
        </div>
    );
};

/*
    return (
        user ? (
            <div className="text-center text-xl mt-36">
                CART PAGE
            </div>
        ) : (
            <div className="text-center text-xl mt-36">
                CART PAGE NOT LOGGED IN
            </div>
        )
    );
*/

export default CartPage;