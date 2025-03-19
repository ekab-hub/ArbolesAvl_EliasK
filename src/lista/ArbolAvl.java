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

    public void agregaAVL(T elem) {
        NodoAVL<T> actual = raiz;
        NodoAVL<T> papa = raiz;
        boolean encontre = false; 
        NodoAVL<T> nuevo = new NodoAVL<T>(elem);

        while (actual != null) {
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


    private NodoAVL<T> roto(NodoAVL<T> actual) {
        NodoAVL<T> papa = actual.papa;
        NodoAVL<T> alfa, beta, gamma, A, B, C, D;

        if (actual.fe == -2 && actual.izq.fe <= 0) { // Caso izquierda-izquierda 
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

         if (actual.fe == 2 && actual.der.fe >= 0) { // Caso derecha-derecha 
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

         if (actual.fe == 2 && actual.der.fe == -1) { // Caso derecha-izquierda 
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

         if (actual.fe == -2 && actual.izq.fe == 1) { // Caso izquierda-derecha 
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


    private void actualizafe() {
        actualizarFe(raiz); 
    }

    private void actualizarFe(NodoAVL<T> actual) {
        if (actual == null) {
            return; // Caso base: si el nodo es nulo, terminamos
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


    private int altura(NodoAVL<T> nodo) {
        if (nodo == null) {
            return 0; 
        }
        return 1 + Math.max(altura(nodo.izq), altura(nodo.der));
    }


    public NodoAVL<T> busca(NodoAVL<T> actual, T elem){
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

    private NodoAVL<T> borraAVL(T elem) {
         NodoAVL<T> actual = busca(raiz, elem);
         if (actual == null) {
             return null; 
         }

         NodoAVL<T> papa = actual.papa;
         NodoAVL<T> hijo;

         if (actual.izq == null && actual.der == null) { 
             if (actual == raiz) {
                 raiz = null; 
             } else {
                 if (papa.izq == actual) {
                     papa.izq = null;
                 } else {
                     papa.der = null;
                 }
             }
         } else if (actual.izq == null) { 
             hijo = actual.der;

             if (actual == raiz) {
                 raiz = hijo; 
             } else {
                 if (papa.izq == actual) {
                     papa.izq = hijo;
                 } else {
                     papa.der = hijo;
                 }
             }
             hijo.papa = papa; 
         } else if (actual.der == null) { 
             hijo = actual.izq;

             if (actual == raiz) {
                 raiz = hijo;
             } else {
                 if (papa.izq == actual) {
                     papa.izq = hijo;
                 } else {
                     papa.der = hijo;
                 }
             }
             hijo.papa = papa;
         } else { // Caso 3: Tiene dos hijos
             NodoAVL<T> sucesor = actual.der;
             while (sucesor.izq != null) {
                 sucesor = sucesor.izq;
             }

             actual.elem = sucesor.elem; 

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


    public void ImprimePorNivel() {
        if (raiz == null) {
         System.out.println("Árbol vacío");
         return;
        }

        Queue<NodoAVL<T>> col = new LinkedList<>();
        col.offer(raiz);
        col.offer(null);

        NodoAVL<T> aux;

        while (!col.isEmpty()) {
            aux = col.remove();

            if (aux == null) { 
                System.out.println(); 
                if (!col.isEmpty()) {
                    col.add(null); 
                }
            } else {
                System.out.print(aux.elem + "(FE: " + aux.fe + ")  "); 

                if (aux.izq != null) {
                    col.add(aux.izq);
                }
                if (aux.der != null) {
                    col.add(aux.der);
                }
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