import javax.print.DocFlavor

/**
 * You MUST implement this interface.
 */
interface VendingMachine {
    /**
     * User Function - This is called when a user presses a button for a particular product. This is used for both price
     * checking and purchasing.
     */
    fun buttonPress(productPosition: Int?)

    /**
     * User Function - This is called when the user adds money to the machine. The cents parameter represent the value
     * of the particular currency added to the machine. For example, when the user adds a Nickel, this function will be
     * called with a value of 5.
     *
     *
     * Note: Only one coin will be added at a time. Only Nickels, Dimes, and Quarters will be added.
     */
    fun addUserMoney(cents: Int?)
}

/**
 * This interface is intended to abstract the interaction with the vending machine's user interface.
 * <p>
 * Note: These functions do not validate if they function correctly, meaning you can call `dispenseProduct` even if the
 * machine does not contain any products, however you should take care to NOT make a call the cannot be completed.
 * Example: do not call `dispenseProduct` for a product that is out of stock.
 */
interface VendingMachineHardwareFunctions {
    fun showMessage(message: String) {
        println(message)
    }

    fun dispenseProduct(productPosition: Int?, productName: String?) {
        val nullSafeProductName = productName ?: "ProductNum"+productPosition!!
        println("Dispensing $nullSafeProductName from position $productPosition")
    }

    fun dispenseChange(changeInCents: Int?) {
        println("Dispensing $changeInCents cents")
    }
}

interface VendingMachineAdminFunctions {
    fun addProduct(product: Product?) {
        TODO()
    }
}

class VendingMachineHardwareFunctionsImpl : VendingMachineHardwareFunctions
class VendingMachineAdminFunctionsImpl: VendingMachineAdminFunctions

data class Product(val name: String, val price: Int, var count: Int)

class VendingMachineImpl(var products: Map<Int, Product>) : VendingMachine {
    var hardwareFunctions = VendingMachineHardwareFunctionsImpl()
    var adminFunctionsImpl = VendingMachineAdminFunctionsImpl()

    var userMoney: Int = 0

    override fun buttonPress(productPosition: Int?) {
        val price = products[productPosition]?.price
        val name = products[productPosition]?.name
        val count = products[productPosition]?.count

        hardwareFunctions.showMessage("Name is $name, amount is $price")

        if (userMoney < price!!) {
            println("Not enough money")
            return
        }
        if (count!! < 1) {
            println("Out of stock")
            return
        }

        hardwareFunctions.dispenseProduct(productPosition, name)
        hardwareFunctions.dispenseChange(userMoney - price)
        products[productPosition]?.count = products[productPosition]?.count!! - 1
        userMoney = 0
    }

    override fun addUserMoney(cents: Int?) {
        userMoney += cents!!
    }
}

fun main(args: Array<String>) {
    val obj = VendingMachineImpl(hashMapOf(0 to Product("foo", 10, 5), 1 to Product("bar", 45, 1)))

    println("Calling addUserMoney(25): ${obj.addUserMoney(25)}")
    println("Calling addUserMoney(25): ${obj.addUserMoney(25)}")
    println("Calling buttonPress(1): ${obj.buttonPress(1)}")
    println("Calling addUserMoney(25): ${obj.addUserMoney(25)}")
    println("Calling addUserMoney(25): ${obj.addUserMoney(25)}")
    println("Calling buttonPress(0): ${obj.buttonPress(1)}")
}