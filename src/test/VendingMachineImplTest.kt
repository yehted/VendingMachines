package test

import org.junit.Test
import org.junit.Assert
import vendingmachine.VendingMachineAdminFunctionsImpl
import vendingmachine.VendingMachineHardwareFunctionsImpl
import vendingmachine.VendingMachineImpl
import vendingmachine.VendingProduct
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class VendingMachineImplTest {
    var startProducts = hashMapOf(0 to VendingProduct("foo", 10, 0), 1 to VendingProduct("bar", 45, 1))

    private val machine = VendingMachineImpl(startProducts, VendingMachineHardwareFunctionsImpl(), VendingMachineAdminFunctionsImpl())

    @Test fun buttonPressShowNameAndPrice() {
        var outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        machine.buttonPress(1 )
        Assert.assertEquals("Name is bar, price is 45\r\nNot enough money\r\n", outContent.toString() )
    }

    @Test fun buttonPressDispense() {
        var outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        machine.addUserMoney(50 )
        machine.buttonPress(1 )
        Assert.assertEquals("Name is bar, price is 45\r\nDispensing bar from position 1\r\nDispensing 5 cents\r\n", outContent.toString() )
        Assert.assertEquals(0, machine.products[0]?.count)
    }

    @Test fun buttonPressOutOfStock() {
        var outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        machine.addUserMoney(50 )
        machine.buttonPress(0 )
        Assert.assertEquals("Name is foo, price is 10\r\nOut of stock\r\n", outContent.toString() )
    }

    @Test fun buttonPressNotEnoughMoney() {
        var outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        machine.addUserMoney( 25 )
        machine.buttonPress(1 )
        Assert.assertEquals("Name is bar, price is 45\r\nNot enough money\r\n", outContent.toString() )
    }

    @Test fun addUserMoney() {
        machine.addUserMoney(50 )
        Assert.assertEquals(50, machine.userMoney )
    }

    @Test fun addProduct() {
        machine.addProduct(0, 1 )
        Assert.assertEquals(1, machine.products[0]?.count )
    }
}