package es.deusto.sd.verificacionfacial;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


class VerificadorBiometricoTest {

    @Test
    public void aceptaDiferenciaEnElLimite() {

        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {0.14};
        double[] capturado = {0.12};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobacion de la tolerancia
        assertTrue(result);
        //Comprobar que 0.14 y 0.12 devuelven true al tener una diferencia de 0.02
        //Antes devolvian false por la precision de los double, ahora lo compensa el margen
    }

    @Test
    public void rechazaDiferenciaSuperiorALaTolerancia() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {0.15};
        double[] capturado = {0.12};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobacion de la tolerancia
        assertFalse(result);
    }

    @Test
    public void longitudesDiferentes() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {0.12, 0.11};
        double[] capturado = {0.12};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobar que rechaza vectores de distinta longitud
        assertFalse(result);
    }

    @Test
    public void vectoresVacios() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {};
        double[] capturado = {};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobar que rechaza vectores vacios
        assertFalse(result);
    }

    @Test
    public void vectoresIguales() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {0.17};
        double[] capturado = {0.17};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobar que acepta dos vectores iguales
        assertTrue(result);
    }

    @Test
    public void vectorCapturadoNull() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {0.16};
        double[] capturado = null;

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobar que rechaza por el vector null
        assertFalse(result);
    }

    @Test
    public void vectorReferenciaNull() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = null;
        double[] capturado = {0.16};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobar que rechaza por el vector null
        assertFalse(result);
    }

    @Test
    public void vectorCapturadoNaN() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {0.15};
        double[] capturado = {Double.NaN};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobar que rechaza por el vector Nan
        assertFalse(result);
    }

    @Test
    public void vectorReferenciaNaN() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] referencia = {Double.NaN};
        double[] capturado = {0.15};

        //Guardar resultado
        boolean result = vb.verificar(referencia, capturado);

        //Comprobar que rechaza por el vector Nan
        assertFalse(result);
    }

    @Test
    public void vectoresConInfinito() {
        //Objeto de la clase
        VerificadorBiometrico vb = new VerificadorBiometrico();

        //Datos de prueba
        double[] valido = {0.15};
        double[] positivo = {Double.POSITIVE_INFINITY};
        double[] negativo = {Double.NEGATIVE_INFINITY};

        //Comprobar que rechaza infinitos en cualquiera de los dos vectores
        assertFalse(vb.verificar(positivo, valido));
        assertFalse(vb.verificar(valido, positivo));
        assertFalse(vb.verificar(negativo, valido));
        assertFalse(vb.verificar(valido, negativo));
        //Comprobar que tampoco acepta dos infinitos iguales
        assertFalse(vb.verificar(positivo, positivo));
        assertFalse(vb.verificar(negativo, negativo));
    }
}
