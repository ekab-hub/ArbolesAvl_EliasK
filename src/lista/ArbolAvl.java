/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lista;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author eliaskablym
 */
public class ArbolAvl<T extends Comparable <T>>{
    NodoAVL<T> raiz;
    int cont;

    public ArbolAvl(NodoAVL<T> raiz){
        this.raiz = raiz;
    }

    public void agregaAVL(T elem) { // Agrega un nuevo elemento al arbol
        NodoAVL<T> actual = raiz;
        NodoAVL<T> papa = raiz;
        boolean encontre = false; 
        NodoAVL<T> nuevo = new NodoAVL<T>(elem);

        while (actual != null) { // Dado que el AVL es un ABB, buscamos la posicion donde debe de insertarse el nuevo elemento
             papa = actual;
             if (elem.compareTo(actual.elem) < 0) {
                 actual = actual.izq;
             } else if (elem.compareTo(actual.elem) > 0) {
                 actual = actual.der;
             } else {
                 return; 
             }
         }

         if (papa == null) {
             raiz = nuevo;
         } else if (elem.compareTo(papa.elem) < 0) {
             papa.izq = nuevo;
         } else {
             papa.der = nuevo;
         }

         nuevo.papa = papa;
         cont++;

         boolean termine = false;
         actual = nuevo;

         while (!termine && actual.papa != null) {
             papa = actual.papa;

             if (actual.elem.compareTo(papa.elem) < 0) {
                 papa.fe--;
             } else {
                 papa.fe++;
             }

             if (papa.fe == 0) {
                 termine = true;
             } else if (papa.fe == 2 || papa.fe == -2) {
                 NodoAVL<T> nuevaRaiz = roto(papa); 
                 if (nuevaRaiz.papa == null) {
                     raiz = nuevaRaiz; 
                 }
                 termine = true;
             }

             actual = papa;
         }
    }


    private NodoAVL<T> roto(NodoAVL<T> actual) { // Método para rotar en caso de encontrar un nodo con FE = {-2, 2}
        NodoAVL<T> papa = actual.papa;
        NodoAVL<T> alfa, beta, gamma, A, B, C, D;

        if (actual.fe == -2 && actual.izq.fe <= 0) { // Caso izq-izq
            alfa = actual;
            beta = alfa.izq;
            gamma = beta.izq;
            A = gamma.izq;
            B = gamma.der;
            C = beta.der;
            D = alfa.der;

            alfa.izq = C;
            if (C != null) C.papa = alfa;
            alfa.der = D;
            if (D != null) D.papa = alfa;

            beta.izq = gamma;
            gamma.papa = beta;
            beta.der = alfa;
            alfa.papa = beta;

            if (papa == null) {
                raiz = beta;
            } else if (beta.elem.compareTo(papa.elem) < 0) {
                papa.izq = beta;
            } else {
                papa.der = beta;
            }
            beta.papa = papa;
            actualizafe();
            return beta;
         }

         if (actual.fe == 2 && actual.der.fe >= 0) { // Caso der-der
             alfa = actual;
             beta = alfa.der;
             gamma = beta.der;
             A = alfa.izq;
             B = beta.izq;
             C = gamma.izq;
             D = gamma.der;

             alfa.izq = A;
             if (A != null) A.papa = alfa;
             alfa.der = B;
             if (B != null) B.papa = alfa;

             beta.izq = alfa;
             alfa.papa = beta;
             beta.der = gamma;
             gamma.papa = beta;

             if (papa == null) {
                 raiz = beta;
             } else if (beta.elem.compareTo(papa.elem) < 0) {
                 papa.izq = beta;
             } else {
                 papa.der = beta;
             }
             beta.papa = papa;

             actualizafe();
             return beta;
         }

         if (actual.fe == 2 && actual.der.fe == -1) { // Caso der-izq
             alfa = actual;
             beta = alfa.der;
             gamma = beta.izq;
             A = alfa.izq;
             B = gamma.izq;
             C = gamma.der;
             D = beta.der;

             alfa.der = B;
             if (B != null) B.papa = alfa;
             beta.izq = C;
             if (C != null) C.papa = beta;

             gamma.izq = alfa;
             alfa.papa = gamma;
             gamma.der = beta;
             beta.papa = gamma;

             if (papa == null) {
                 raiz = gamma;
             } else if (gamma.elem.compareTo(papa.elem) < 0) {
                 papa.izq = gamma;
             } else {
                 papa.der = gamma;
             }
             gamma.papa = papa;

             actualizafe();
             return gamma;
         }

         if (actual.fe == -2 && actual.izq.fe == 1) { // Caso izq-der
             alfa = actual;
             beta = alfa.izq;
             gamma = beta.der;
             A = beta.izq;
             B = gamma.izq;
             C = gamma.der;
             D = alfa.der;

             beta.der = B;
             if (B != null) B.papa = beta;
             alfa.izq = C;
             if (C != null) C.papa = alfa;

             gamma.izq = beta;
             beta.papa = gamma;
             gamma.der = alfa;
             alfa.papa = gamma;

             if (papa == null) {
                 raiz = gamma;
             } else if (gamma.elem.compareTo(papa.elem) < 0) {
                 papa.izq = gamma;
             } else {
                 papa.der = gamma;
             }
             gamma.papa = papa;

             actualizafe();
             return gamma;
         }

         return null;
    }


    private void actualizafe() {  // Actualiza el factor de equilibrio de los nodos, se utiliza en el borra y agrega
        actualizarFe(raiz); 
    }

    private void actualizarFe(NodoAVL<T> actual) {
        if (actual == null) {
            return; 
        }
        actualizarFe(actual.izq);
        actualizarFe(actual.der);

        int alturaIzq;
        if(actual.izq == null){
            alturaIzq = 0;
        }else
            alturaIzq = altura(actual.izq);

        int alturaDer;
        if(actual.der == null){
            alturaDer = 0;
        }else
            alturaDer = altura(actual.der);

        actual.fe = alturaDer - alturaIzq;
    }


    private int altura(NodoAVL<T> nodo) { // Calcula la altura del arbol
        if (nodo == null) {
            return 0; 
        }
        int a1 = altura(nodo.izq);
        int a2 = altura(nodo.der);
        return 1 + Math.max(a1, a2);
    }


    public NodoAVL<T> busca(NodoAVL<T> actual, T elem){ // Busca un elemento en el arbol
        if(actual == null)
            return null;
        if(actual.elem.equals(elem))
            return actual;
        NodoAVL<T> temp = busca(actual.getIzq(), elem);
        if(temp==null){
            temp = busca(actual.getDer(), elem);
        }
        return temp;
    }

    private NodoAVL<T> borraAVL(T elem) {  // borra un elemento dado en el arbol
         NodoAVL<T> actual = busca(raiz, elem);
         if (actual == null) {
             return null; 
         }

         NodoAVL<T> papa = actual.papa;
         NodoAVL<T> hijo;

         if (actual.izq == null && actual.der == null) { // Caso 1) Nodo borrado no tiene hijos
             if (actual == raiz) { // Caso 1.1) Nodo borrado no tiene hijos y es la raiz
                 raiz = null; 
             } else {
                 if (papa.izq == actual) { // Caso 1.1) Nodo borrado no tiene hijos y no es la raiz
                     papa.izq = null;
                 } else {
                     papa.der = null;
                 }
             }
         } else if (actual.izq == null) {  // Caso 2.1) Nodo borrado solo tiene hijo derecho
             hijo = actual.der;

             if (actual == raiz) { // Caso 2.1.1) Nodo borrado solo tiene hijo derecho y es raiz
                 raiz = hijo; 
             } else { // Caso 2.1.1) Nodo borrado solo tiene hijo derecho y no es raiz
                 if (papa.izq == actual) {
                     papa.izq = hijo;
                 } else {
                     papa.der = hijo;
                 }
             }
             hijo.papa = papa; 
         } else if (actual.der == null) { // Caso 2.2) Nodo borrado solo tiene hijo izquierdo
             hijo = actual.izq;

             if (actual == raiz) { // Caso 2.2.1) Nodo borrado solo tiene hijo izquierdo y es raiz
                 raiz = hijo;
             } else { // Caso 2.2.2) Nodo borrado solo tiene hijo izquierdo y no es raiz
                 if (papa.izq == actual) {
                     papa.izq = hijo;
                 } else {
                     papa.der = hijo;
                 }
             }
             hijo.papa = papa;
         } else { // Caso 3) Nodo borrado tiene dos hijos
             NodoAVL<T> sucesor = actual.der;
             while (sucesor.izq != null) { // Encuentro el sucesor inorden
                 sucesor = sucesor.izq;
             }

             actual.elem = sucesor.elem; // Cambio el elemento del nodo borrado por el del sucesor

             if (sucesor.papa != actual) {
                 sucesor.papa.izq = sucesor.der;
             } else {
                 actual.der = sucesor.der;
             }

             if (sucesor.der != null) {
                 sucesor.der.papa = sucesor.papa;
             }

             papa = sucesor.papa; 
         }

         cont--;
         actualizafe(); 
         return actual; 
    }


    public void ImprimePorNivel() { // Método para imprimir el árbol por nivel
        if (raiz == null) { // Caso arbol vacio
         System.out.println("No hay elementos");
         return;
        }

        Queue<NodoAVL<T>> col = new LinkedList<>(); // Creo una cola para manejar los elementos 
        col.add(raiz); 
        col.add(null); // Agrego a la cola la raiz y un null

        NodoAVL<T> aux;
        int nivel = 0;

        System.out.println("Nivel " + nivel + ":"); // Imprime el nivel inicial
        while (!col.isEmpty()) { // Mientras la cola no esta vacia...
            aux = col.remove(); // Saco al elemento más antiguo de la cola

            if (aux == null) { // Si el elemento sacado es null...
                System.out.println("\n-----");; 
                if (!col.isEmpty()) {
                    col.add(null); 
                    nivel++;
                    System.out.println("Nivel " + nivel + ":");
                }
            } else { // Si el elemento sacado NO es null, lo imprimo junto con su factor de equilibrio
                System.out.print(aux.elem + "(FE: " + aux.fe + ")  "); 

                if (aux.izq != null) {
                    col.add(aux.izq);
                }
                if (aux.der != null) {
                    col.add(aux.der);
                } // En caso de que no sean nulos, agrego los hijos del elemento que acabo de imprimir a la cola
            }
        }  
    }





    public static void main(String[] args) {
        ArbolAvl<Integer> arbol = new ArbolAvl<>(new NodoAVL<>(10)); 
         arbol.agregaAVL(2);
         arbol.agregaAVL(8);
         arbol.agregaAVL(23);
         arbol.agregaAVL(70);
         arbol.agregaAVL(1000);
         arbol.agregaAVL(230);
         arbol.agregaAVL(4);
         arbol.agregaAVL(35);
         arbol.agregaAVL(10);

         // Imprimir árbol por niveles
         System.out.println("Arbol AVL impreso por niveles:");
         arbol.ImprimePorNivel();

         arbol.borraAVL(23);

         System.out.println("Arbol AVL impreso por niveles:");
         arbol.ImprimePorNivel();

     }

    

    
      
}
